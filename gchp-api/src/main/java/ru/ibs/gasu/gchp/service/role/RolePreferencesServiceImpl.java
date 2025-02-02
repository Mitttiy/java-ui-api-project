package ru.ibs.gasu.gchp.service.role;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.gchp.domain.RolePreferences;
import ru.ibs.gasu.gchp.domain.UserRole;
import ru.ibs.gasu.gchp.user.UserContext;
import ru.ibs.gasu.soap.generated.user.EsiaUserData;
import ru.ibs.gasu.soap.generated.user.PublicUserWebService;

import java.util.Set;

import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.ibs.gasu.dictionaries.service.DictionaryDataService.RF_ID;
import static ru.ibs.gasu.gchp.domain.OrgLevel.*;
import static ru.ibs.gasu.gchp.domain.RolePreferences.*;
import static ru.ibs.gasu.gchp.domain.UserRole.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePreferencesServiceImpl implements RolePreferencesService {
    private final PublicUserWebService userService;
    private final SvrOrgService svrOrgService;

    @Value("${curatorZkhForm}")
    private Long curatorZkhForm;

    @Value("${curatorZkhSphere}")
    private Long curatorZkhSphere;

    @Value("${curatorTsSphere}")
    private Long curatorTsSphere;

    @Value("${curatorOzForm}")
    private Long curatorOzForm;

    @Value("${curatorOzSector}")
    private Long curatorOzSector;

    @Value("${curatorOzSphere}")
    private Long curatorOzSphere;

    @Value("${curatorFinLevel}")
    private Long curatorFinLevel;

    @Override
    public RolePreferences getRolePreferences() {
        RolePreferences preferences = new RolePreferences();

        String ogrn;

        Long userId = UserContext.get().getUserId();

        if (!isEmpty(userId)) {
            setRoles(preferences, userId);

            if (!isAccess(preferences)) {
                preferences.setAccess(false);
                preferences.setAccessErr("<div>Не удалось идентифицировать организацию пользователя .</div><br/><br/>");
                return preferences;
            }

            EsiaUserData esiaUserData = userService.getEsiaUserData(userId);
            if (isEmpty(esiaUserData) || isEmpty(ogrn = esiaUserData.getEsiaOrgOGRN())) {
                preferences.setAccess(false);
                preferences.setAccessErr("Не найден ОГРН в Вашем профиле ЕСИА – обратитесь в службу технической поддержки ГАС «Управление»");
                return preferences;
            } else {
                preferences.setOgrn(esiaUserData.getEsiaOrgOGRN());
                for (SvrOrg svrOrg : svrOrgService.getSvrOrgsByOgrn(ogrn)) {
                    if (svrOrg.getPeriodEnd() == null) {
                        preferences.getOrgIds().add(svrOrg.getId());
                    } else {
                        continue;
                    }

                    if ("1".equals(svrOrg.getIsFoiv()) && !isEmpty(svrOrg.getSp2())) {
                        preferences.setOrgLevel(FOIV);
                        preferences.setRegionId(RF_ID);
                    }

                    if ("2".equals(svrOrg.getLevelType()) && !isEmpty(svrOrg.getSp1())) {
                        preferences.setRegionId(svrOrg.getSp1());
                        preferences.setOrgLevel(ROIV);
                    }

                    if ("3".equals(svrOrg.getLevelType()) && !isEmpty(svrOrg.getSp1())) {
                        preferences.setRegionId(svrOrg.getSp1());
                        preferences.setOktmoCode(svrOrg.getOktmoCode());
                        preferences.setOrgLevel(OMSU);

                        preferences.setFilterByOktmoDocumentForm(true);
                        if (preferences.isUser()) {
                            preferences.setFilterByOktmo(true);
                        }
                    }
                }
            }
        }

        if (!isAccessConcendent(preferences) && !isAccessController(preferences)) {
            preferences.setAccess(false);
            preferences.setAccessErr("<div>Не удалось идентифицировать организацию пользователя .</div><br/><br/>");
            return preferences;
        }
        return preferences;
    }

    private boolean isAccess(RolePreferences preferences) {
        return preferences.isAdmin() || !preferences.getUserRoles().isEmpty();
    }

    private boolean isAccessConcendent(RolePreferences preferences) {
        return !isGchpUser(preferences.getUserRoles()) || preferences.getOrgLevel() != null;
    }

    private boolean isAccessController(RolePreferences preferences) {
        return !isGchpController(preferences.getUserRoles()) || !ROIV.equals(preferences.getOrgLevel());
    }

    private boolean isGchpUser(Set<UserRole> userRoles) {
        return userRoles.contains(GCHPUser);
    }

    private boolean isGchpController(Set<UserRole> userRoles) {
        return userRoles.contains(GCHPController);
    }

    //метод определения ролей пользователя,
    //допускается, что пользователь может иметь несколько ролей одновременно.
    private void setRoles(RolePreferences preferences, Long userId) {
        if (userService.isUserAdmin(userId)) {
            preferences.setAdmin(true);
        }

        userService.getUserRoles(userId).forEach(role -> {
            if (isMinistryCurator(role.getActualCode())) {
                preferences.getUserRoles().add(MinistryCuratorGCHP);
            }

            if (isTSCurator(role.getActualCode())) {
                preferences.getUserRoles().add(TSCuratorGCHP);
            }

            if (isOZCurator(role.getActualCode())) {
                preferences.getUserRoles().add(OZCuratorGCHP);
            }

            if (isFinCurator(role.getActualCode())) {
                preferences.getUserRoles().add(FinCuratorGCHP);
            }

            if (isControllerRoiv(role.getActualCode())) {
                preferences.getUserRoles().add(GCHPController);
            }

            if (isConcendent(role.getActualCode())) {
                preferences.getUserRoles().add(GCHPUser);
            }

            if (isController(role.getActualCode())) {
                preferences.getUserRoles().add(GCHPAnalysts);
            }

            if (isCurator(role.getActualCode())) {
                preferences.getUserRoles().add(GCHPAdministrator);

            }
        });

        /*Куратор ЖКХ - Минстрой России ОГРН 1127746554320 -
        просмотр проектов форма реализации = 115 ФЗ
        сфера реализации = Коммунально-энергетическая
        GASU_ZP_18*/
        if (preferences.getUserRoles().contains(MinistryCuratorGCHP)) {
            preferences.setFilterByRealizationForm(true);
            preferences.setFilterByRealizationSphere(true);

            preferences.setRealizationForm(curatorZkhForm);
            preferences.setRealizationSphere(curatorZkhSphere);
        }

        /*Куратор транспортной сферы - Минтранс России ОГРН 1047702023599
         просмотр проектов сфера реализации = Транспортная
         GASU_ZP_25*/
        if (preferences.getUserRoles().contains(TSCuratorGCHP)) {
            preferences.setFilterByRealizationSphere(true);

            preferences.setRealizationSphere(curatorTsSphere);
        }

         /*Куратор объектов здравоохранения - Минздрав России ОГРН 1127746460896
         просмотр проектов форма реализации = 224 ФЗ
         отрасль реализации = здравоохранение
         GASU_ZP_27*/
        if (preferences.getUserRoles().contains(OZCuratorGCHP)) {
            preferences.setFilterByRealizationForm(true);
            preferences.setFilterByRealizationSphere(true);
            preferences.setFilterByRealizationSector(true);

            preferences.setRealizationForm(curatorOzForm);
            preferences.setRealizationSphere(curatorOzSphere);
            preferences.setRealizationSector(curatorOzSector);
        }

        /*Куратор ГЧП финансы - Минфин России ОГРН 1037739085636
        просмотр проектов уровень реализации = Федеральный
        GASU_ZP_48*/
        if (preferences.getUserRoles().contains(FinCuratorGCHP)) {
            preferences.setFilterByRealizationLevel(true);

            preferences.setRealizationLevel(curatorFinLevel);
        }

        /*Контролер ГЧП - РОИВ
        ввод, просмотр, редактирование, удаление черновиков проектов своего региона
        GASU_ZP_19*/
        boolean isGCHPController = preferences.getUserRoles().contains(GCHPController);
        /*Концедент (публичный партнер ГЧП) - ФОИВ, РОИВ, ОМСУ
        ввод, просмотр, редактирование, удаление черновиков проектов своей организации
        GASU_ZP_16*/
        boolean isGCHPUser = preferences.getUserRoles().contains(GCHPUser);
        /*Контролер ГЧП (все проекты) - ФАС России ОГРН 1047796269663
        просмотр всех проектов
        GASU_ZP_28*/
        boolean isGCHPAnalysts = preferences.getUserRoles().contains(GCHPAnalysts);
        /*Куратор ГЧП - Минэкономразвития России ОГРН 1027700575385
        ввод, просмотр, редактирование и удаление любых проектов любых организаций (полный доступ)
        GASU_ZP_17*/
        boolean isGCHPAdministrator = preferences.getUserRoles().contains(GCHPAdministrator);

        preferences.setCanChangeStatus(isGCHPController || isGCHPAdministrator);
        preferences.setCanDoStatusDraft(isGCHPAdministrator);
        if (isGCHPController || isGCHPUser || isGCHPAnalysts || isGCHPAdministrator || preferences.isAdmin()) {
            //если в условиях выше было установлено значение ИСТИНА,
            //то необходимо сбросить.
            preferences.setFilterByRealizationForm(false);
            preferences.setFilterByRealizationSphere(false);
            preferences.setFilterByRealizationSector(false);
            preferences.setFilterByRealizationLevel(false);

            if (!isGCHPAnalysts) {
                preferences.setCanEdit(true);
                preferences.setCanDelete(true);

                //регистрировать новые проекты может только GCHPUser
                preferences.setCanRegistry(isGCHPUser);

                if(isGCHPController || isGCHPUser) {
                    preferences.setFilterByRegionDocumentForm(true);
                }

                if(isGCHPUser) {
                    preferences.setFilterByOrgDocumentForm(true);
                }

                if (!isGCHPAdministrator && !preferences.isAdmin()) {
                    preferences.setFilterByRegion(true);

                    if (!isGCHPController) {
                        preferences.setFilterByOrg(true);

                        //имеет только одну роль GCHPUser
                        preferences.setUser(true);
                    }
                }
            }
        }
    }
}
