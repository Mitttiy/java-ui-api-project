function certMetaData(subject, issuer, from, till, provname, algorithm, snils) {
    this.subjectName = subject;
    this.issuer = issuer;
    this.validFrom = from;
    this.validTo = till;
    this.type = provname;
    this.signAlg = algorithm;
    this.snils = snils;
}

var certNameList = [];
var certMetaList = [];
var certThumbprintList = [];
var certList = [];
var callback = {
    initCompleteSuccess: function(certList) {

    },
    initCompleteFailure: function(message) {

    },
    signCompleteSuccess: function(signedText) {

    },
    signCompleteFailure: function(message) {

    },
    loadAsyncCode: function() {

    }
}

function getSnilsArr(subjectName) {
    var snilsArr = subjectName.match(/OID.1.2.643.100.3=[0-9]*/);
    if ((!snilsArr)||(snilsArr.length===0)){
        snilsArr = subjectName.match(/SNILS=[0-9]*/);
        if ((!snilsArr)||(snilsArr.length===0)){
            snilsArr = subjectName.match(/СНИЛС=[0-9]*/);
        }
    }
    return snilsArr;
}

var isPluginEnabled = false;
var fileContent; // Переменная для хранения информации из файла, значение присваивается в cades_bes_file.html
function getXmlHttp(){
    var xmlhttp;
    try {
        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        try {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (E) {
            xmlhttp = false;
        }
    }
    if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
        xmlhttp = new XMLHttpRequest();
    }
    return xmlhttp;
}

var async_code_included = 0;
var async_Promise;
var async_resolve;
/*
function include_async_code()
{
    if(async_code_included)
    {
        return async_Promise;
    }
    var fileref = document.createElement('script');
    fileref.setAttribute("type", "text/javascript");
    //fileref.setAttribute("src", "http://localhost:8888/js/async_code.js");
    fileref.setAttribute("src", "/js/async_code.js");
    document.getElementsByTagName("head")[0].appendChild(fileref);
    async_Promise = new Promise(function(resolve, reject){
        async_resolve = resolve;
    });
    async_code_included = 1;
    return async_Promise;
}
*/

function include_async_code()
{

    if(async_code_included)
    {
        return async_Promise;
    }

    async_Promise = new Promise(function(resolve, reject){
        async_resolve = resolve;
    });

    async_code_included = 1;

    callback.loadAsyncCode();

    return async_Promise;
}


function Common_RetrieveCertificate()
{
    var canAsync = !!cadesplugin.CreateObjectAsync;
    if(canAsync)
    {
        include_async_code().then(function(){
            return RetrieveCertificate_Async();
        });
    }else
    {
        return RetrieveCertificate_NPAPI();
    }
}

function Common_CreateSimpleSign(id)
{
    var canAsync = !!cadesplugin.CreateObjectAsync;
    if(canAsync)
    {
        include_async_code().then(function(){
            return CreateSimpleSign_Async(id);
        });
    }else
    {
        return CreateSimpleSign_NPAPI(id);
    }
}

function Common_SignCadesBES(thumbprint, text, setDisplayData)
{
    var canAsync = !!cadesplugin.CreateObjectAsync;
    if(canAsync)
    {
        include_async_code().then(function(){
            return SignCadesBES_Async(thumbprint, text, setDisplayData);
        });
    } else {

        return SignCadesBES_NPAPI(thumbprint, text, setDisplayData);
    }
}

function Common_SignCadesBES_File(id) {
    var canAsync = !!cadesplugin.CreateObjectAsync;
    if (canAsync) {
        include_async_code().then(function () {
            return SignCadesBES_Async_File(id);
        });
    } else {
        return SignCadesBES_NPAPI_File(id);
    }
}

function Common_SignCadesXLong(id)
{
    var canAsync = !!cadesplugin.CreateObjectAsync;
    if(canAsync)
    {
        include_async_code().then(function(){
            return SignCadesXLong_Async(id);
        });
    }else
    {
        return SignCadesXLong_NPAPI(id);
    }
}

function Common_SignCadesXML(id)
{
    var canAsync = !!cadesplugin.CreateObjectAsync;
    if(canAsync)
    {
        include_async_code().then(function(){
            return SignCadesXML_Async(id);
        });
    }else
    {
        return SignCadesXML_NPAPI(id);
    }
}

function Common_CheckForPlugIn() {

//    cadesplugin.set_log_level(cadesplugin.LOG_LEVEL_DEBUG);
    var canAsync = !!cadesplugin.CreateObjectAsync;
    if(canAsync) {

        include_async_code().then(function(){
            return CheckForPlugIn_Async();
        });
    } else {

        return CheckForPlugIn_NPAPI();
    }
}

function Common_Encrypt() {
    var canAsync = !!cadesplugin.CreateObjectAsync;
    if(canAsync)
    {
        include_async_code().then(function(){
            return Encrypt_Async();
        });
    }else
    {
        return Encrypt_NPAPI();
    }
}

function Common_Decrypt(id) {
    var canAsync = !!cadesplugin.CreateObjectAsync;
    if(canAsync)
    {
        include_async_code().then(function(){
            return Decrypt_Async(id);
        });
    }else
    {
        return Decrypt_NPAPI(id);
    }
}

function GetCertificate_NPAPI(thumbprint) {
    try {
        var oStore = cadesplugin.CreateObject("CAdESCOM.Store");
        oStore.Open();
    } catch (err) {
        alert('Failed to create CAdESCOM.Store: ' + cadesplugin.getLastError(err));
        return;
    }

    var CAPICOM_CERTIFICATE_FIND_SHA1_HASH = 0;
    var oCerts = oStore.Certificates.Find(CAPICOM_CERTIFICATE_FIND_SHA1_HASH, thumbprint);

    if (oCerts.Count == 0) {
        alert("Certificate not found");
        return;
    }
    var oCert = oCerts.Item(1);
    return oCert;
}

function FillCertInfo_NPAPI(certificate, certBoxID)
{
    var BoxID;
    var field_prefix;
    if(typeof(certBoxID) == 'undefined')
    {
        BoxID = 'cert_info';
        field_prefix = '';
    }else {
        BoxID = certBoxID;
        field_prefix = certBoxID;
    }

    var certObj = new CertificateObj(certificate);
    document.getElementById(BoxID).style.display = '';
    document.getElementById(field_prefix + "subject").innerHTML = "Владелец: <b>" + certObj.GetCertName() + "<b>";
    document.getElementById(field_prefix + "issuer").innerHTML = "Издатель: <b>" + certObj.GetIssuer() + "<b>";
    document.getElementById(field_prefix + "from").innerHTML = "Выдан: <b>" + certObj.GetCertFromDate() + "<b>";
    document.getElementById(field_prefix + "till").innerHTML = "Действителен до: <b>" + certObj.GetCertTillDate() + "<b>";
    document.getElementById(field_prefix + "provname").innerHTML = "Криптопровайдер: <b>" + certObj.GetPrivateKeyProviderName() + "<b>";
    document.getElementById(field_prefix + "algorithm").innerHTML = "Алгоритм ключа: <b>" + certObj.GetPubKeyAlgorithm() + "<b>";
}

function MakeCadesBesSign_NPAPI(dataToSign, certObject, setDisplayData, isBase64) {
    var errormes = "";

    try {
        var oSigner = cadesplugin.CreateObject("CAdESCOM.CPSigner");
    } catch (err) {
        errormes = "Failed to create CAdESCOM.CPSigner: " + err.number;
        alert(errormes);
        throw errormes;
    }

    if (oSigner) {
        oSigner.Certificate = certObject;
    }
    else {
        errormes = "Failed to create CAdESCOM.CPSigner";
        alert(errormes);
        throw errormes;
    }

    try {
        var oSignedData = cadesplugin.CreateObject("CAdESCOM.CadesSignedData");
    } catch (err) {
        alert('Failed to create CAdESCOM.CadesSignedData: ' + err.number);
        return;
    }

    var CADES_BES = 1;
    var Signature;

    if (dataToSign) {
        // Данные на подпись ввели
        oSignedData.ContentEncoding = 1; //CADESCOM_BASE64_TO_BINARY
        if(typeof(setDisplayData) != 'undefined')
        {
            //Set display data flag flag for devices like Rutoken PinPad
            oSignedData.DisplayData = 1;
        }
	if (typeof(isBase64) == 'undefined'){
            oSignedData.Content = Base64.encode(dataToSign);
        } else {
            oSignedData.Content = dataToSign;
        }
        oSigner.Options = 1; //CAPICOM_CERTIFICATE_INCLUDE_WHOLE_CHAIN
        try {
            Signature = oSignedData.SignCades(oSigner, CADES_BES);
        }
        catch (err) {
            errormes = "Не удалось создать подпись из-за ошибки: " + cadesplugin.getLastError(err);
            alert(cadesplugin.getLastError(err));
            throw errormes;
        }
    }
    return Signature;
}

function MakeCadesXLongSign_NPAPI(dataToSign, tspService, certObject) {
    var errormes = "";

    try {
        var oSigner = cadesplugin.CreateObject("CAdESCOM.CPSigner");
    } catch (err) {
        errormes = "Failed to create CAdESCOM.CPSigner: " + err.number;
        alert(errormes);
        throw errormes;
    }

    if (oSigner) {
        oSigner.Certificate = certObject;
    }
    else {
        errormes = "Failed to create CAdESCOM.CPSigner";
        alert(errormes);
        throw errormes;
    }

    try {
        var oSignedData = cadesplugin.CreateObject("CAdESCOM.CadesSignedData");
    } catch (err) {
        alert('Failed to create CAdESCOM.CadesSignedData: ' + cadesplugin.getLastError(err));
        return;
    }

    var CADESCOM_CADES_X_LONG_TYPE_1 = 0x5d;
    var Signature;

    if (dataToSign) {
        // Данные на подпись ввели
        oSignedData.Content = dataToSign;
        oSigner.Options = 1; //CAPICOM_CERTIFICATE_INCLUDE_WHOLE_CHAIN
        oSigner.TSAAddress = tspService;
        try {
            Signature = oSignedData.SignCades(oSigner, CADESCOM_CADES_X_LONG_TYPE_1);
        }
        catch (err) {
            errormes = "Не удалось создать подпись из-за ошибки: " + cadesplugin.getLastError(err);
            alert(errormes);
            throw errormes;
        }
    }
    return Signature;
}

function MakeXMLSign_NPAPI(dataToSign, certObject) {
    try {
        var oSigner = cadesplugin.CreateObject("CAdESCOM.CPSigner");
    } catch (err) {
        errormes = "Failed to create CAdESCOM.CPSigner: " + err.number;
        alert(errormes);
        throw errormes;
    }

    if (oSigner) {
        oSigner.Certificate = certObject;
    }
    else {
        errormes = "Failed to create CAdESCOM.CPSigner";
        alert(errormes);
        throw errormes;
    }

    var signMethod = "";
    var digestMethod = "";

    var pubKey = certObject.PublicKey();
    var algo = pubKey.Algorithm;
    var algoOid = algo.Value;
    if (algoOid == "1.2.643.7.1.1.1.1") {   // алгоритм подписи ГОСТ Р 34.10-2012 с ключом 256 бит
        signMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102012-gostr34112012-256";
        digestMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34112012-256";
    }
    else if (algoOid == "1.2.643.7.1.1.1.2") {   // алгоритм подписи ГОСТ Р 34.10-2012 с ключом 512 бит
        signMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102012-gostr34112012-512";
        digestMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34112012-512";
    }
    else if (algoOid == "1.2.643.2.2.19") {  // алгоритм ГОСТ Р 34.10-2001
        signMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102001-gostr3411";
        digestMethod = "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr3411";
    }
    else {
        errormes = "Данная демо страница поддерживает XML подпись сертификатами с алгоритмом ГОСТ Р 34.10-2012, ГОСТ Р 34.10-2001";
        throw errormes;
    }
    
    var CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED = 0;

    try {
        var oSignedXML = cadesplugin.CreateObject("CAdESCOM.SignedXML");
    } catch (err) {
        alert('Failed to create CAdESCOM.SignedXML: ' + cadesplugin.getLastError(err));
        return;
    }

    oSignedXML.Content = dataToSign;
    oSignedXML.SignatureType = CADESCOM_XML_SIGNATURE_TYPE_ENVELOPED;
    oSignedXML.SignatureMethod = signMethod;
    oSignedXML.DigestMethod = digestMethod;

    var sSignedMessage = "";
    try {
        sSignedMessage = oSignedXML.Sign(oSigner);
    }
    catch (err) {
        errormes = "Не удалось создать подпись из-за ошибки: " + cadesplugin.getLastError(err);
        alert(errormes);
        throw errormes;
    }

    return sSignedMessage;
}

function GetSignatureTitleElement()
{
    var elementSignatureTitle = null;
    var x = document.getElementsByName("SignatureTitle");

    if(x.length == 0)
    {
        elementSignatureTitle = document.getElementById("SignatureTxtBox").parentNode.previousSibling;

        if(elementSignatureTitle.nodeName == "P")
        {
            return elementSignatureTitle;
        }
    }
    else
    {
        elementSignatureTitle = x[0];
    }

    return elementSignatureTitle;
}

function SignCadesBES_NPAPI(thumbprint, text, setDisplayData) {

    var certificate = GetCertificate_NPAPI(thumbprint);

    try {
        //FillCertInfo_NPAPI(certificate);

        var signature = MakeCadesBesSign_NPAPI(text, certificate, setDisplayData);

        callback.signCompleteSuccess(signature);

    } catch(err) {
        callback.signCompleteFailure(err);
    }
}

function SignCadesBES_NPAPI_File(certListBoxId) {
    var certificate = GetCertificate_NPAPI(certListBoxId);
    var dataToSign = fileContent;
    var x = GetSignatureTitleElement();
    try {
        FillCertInfo_NPAPI(certificate);
        var StartTime = Date.now();
	var setDisplayData;
        var signature = MakeCadesBesSign_NPAPI(dataToSign, certificate, setDisplayData, 1);
        var EndTime = Date.now();
        document.getElementsByName('TimeTitle')[0].innerHTML = "Время выполнения: " + (EndTime - StartTime) + " мс";
        document.getElementById("SignatureTxtBox").innerHTML = signature;
        if (x != null) {
            x.innerHTML = "Подпись сформирована успешно:";
        }
    }
    catch (err) {
        if (x != null) {
            x.innerHTML = "Возникла ошибка:";
        }
        document.getElementById("SignatureTxtBox").innerHTML = err;
    }
}

function SignCadesXLong_NPAPI(certListBoxId) {
    var certificate = GetCertificate_NPAPI(certListBoxId);
    var dataToSign = document.getElementById("DataToSignTxtBox").value;
    var tspService = document.getElementById("TSPServiceTxtBox").value ;
    var x = GetSignatureTitleElement();
    try
    {
        FillCertInfo_NPAPI(certificate);
        var signature = MakeCadesXLongSign_NPAPI(dataToSign, tspService, certificate);
        document.getElementById("SignatureTxtBox").innerHTML = signature;
        if(x!=null)
        {
            x.innerHTML = "Подпись сформирована успешно:";
        }
    }
    catch(err)
    {
        if(x!=null)
        {
            x.innerHTML = "Возникла ошибка:";
        }
        document.getElementById("SignatureTxtBox").innerHTML = err;
    }
}

function SignCadesXML_NPAPI(certListBoxId) {
    var certificate = GetCertificate_NPAPI(certListBoxId);
    var dataToSign = document.getElementById("DataToSignTxtBox").value;
    var x = GetSignatureTitleElement();
    try
    {
        FillCertInfo_NPAPI(certificate);
        var signature = MakeXMLSign_NPAPI(dataToSign, certificate);
        document.getElementById("SignatureTxtBox").innerHTML = signature;

        if(x!=null)
        {
            x.innerHTML = "Подпись сформирована успешно:";
        }
    }
    catch(err)
    {
        if(x!=null)
        {
            x.innerHTML = "Возникла ошибка:";
        }
        document.getElementById("SignatureTxtBox").innerHTML = err;
    }
}

function MakeVersionString(oVer)
{
    var strVer;
    if(typeof(oVer)=="string")
        return oVer;
    else
        return oVer.MajorVersion + "." + oVer.MinorVersion + "." + oVer.BuildVersion;
}

function CheckForPlugIn_NPAPI() {
    function VersionCompare_NPAPI(StringVersion, ObjectVersion) {
        if (typeof(ObjectVersion) == "string")
            return -1;
        var arr = StringVersion.split('.');

        if (ObjectVersion.MajorVersion == parseInt(arr[0])) {
            if (ObjectVersion.MinorVersion == parseInt(arr[1])) {
                if (ObjectVersion.BuildVersion == parseInt(arr[2])) {
                    return 0;
                }
                else if (ObjectVersion.BuildVersion < parseInt(arr[2])) {
                    return -1;
                }
            } else if (ObjectVersion.MinorVersion < parseInt(arr[1])) {
                return -1;
            }
        } else if (ObjectVersion.MajorVersion < parseInt(arr[0])) {
            return -1;
        }

        return 1;
    }

    function GetCSPVersion_NPAPI() {
        try {
            var oAbout = cadesplugin.CreateObject("CAdESCOM.About");
        } catch (err) {
            alert('Failed to create CAdESCOM.About: ' + cadesplugin.getLastError(err));
            return;
        }
        var ver = oAbout.CSPVersion("", 75);
        return ver.MajorVersion + "." + ver.MinorVersion + "." + ver.BuildVersion;
    }

    function GetCSPName_NPAPI() {
        var sCSPName = "";
        try {
            var oAbout = cadesplugin.CreateObject("CAdESCOM.About");
            sCSPName = oAbout.CSPName(75);

        } catch (err) {
        }
        return sCSPName;
    }

    function ShowCSPVersion_NPAPI(CurrentPluginVersion) {
        if (typeof(CurrentPluginVersion) != "string") {
            document.getElementById('CSPVersionTxt').innerHTML = "Версия криптопровайдера: " + GetCSPVersion_NPAPI();
        }
        var sCSPName = GetCSPName_NPAPI();
        if (sCSPName != "") {
            document.getElementById('CSPNameTxt').innerHTML = "Криптопровайдер: " + sCSPName;
        }
    }

    function GetLatestVersion_NPAPI(CurrentPluginVersion) {
        var xmlhttp = getXmlHttp();
        xmlhttp.open("GET", "/sites/default/files/products/cades/latest_2_0.txt", true);
        xmlhttp.onreadystatechange = function () {
            var PluginBaseVersion;
            if (xmlhttp.readyState == 4) {
                if (xmlhttp.status == 200) {
                    PluginBaseVersion = xmlhttp.responseText;
                    if (isPluginWorked) { // плагин работает, объекты создаются
                        if (VersionCompare_NPAPI(PluginBaseVersion, CurrentPluginVersion) < 0) {
                            document.getElementById('PluginEnabledImg').setAttribute("src", "Img/yellow_dot.png");
                            document.getElementById('PlugInEnabledTxt').innerHTML = "Плагин загружен, но есть более свежая версия.";
                        }
                    }
                    else { // плагин не работает, объекты не создаются
                        if (isPluginLoaded) { // плагин загружен
                            if (!isPluginEnabled) { // плагин загружен, но отключен
                                document.getElementById('PluginEnabledImg').setAttribute("src", "Img/red_dot.png");
                                document.getElementById('PlugInEnabledTxt').innerHTML = "Плагин загружен, но отключен в настройках браузера.";
                            }
                            else { // плагин загружен и включен, но объекты не создаются
                                document.getElementById('PluginEnabledImg').setAttribute("src", "Img/red_dot.png");
                                document.getElementById('PlugInEnabledTxt').innerHTML = "Плагин загружен, но не удается создать объекты. Проверьте настройки браузера.";
                            }
                        }
                        else { // плагин не загружен
                            document.getElementById('PluginEnabledImg').setAttribute("src", "Img/red_dot.png");
                            document.getElementById('PlugInEnabledTxt').innerHTML = "Плагин не загружен.";
                        }
                    }
                }
            }
        }
        xmlhttp.send(null);
    }

    var isPluginLoaded = false;
    var isPluginWorked = false;
    var isActualVersion = false;
    try {
        var oAbout = cadesplugin.CreateObject("CAdESCOM.About");
        isPluginLoaded = true;
        isPluginEnabled = true;
        isPluginWorked = true;
        // Это значение будет проверяться сервером при загрузке демо-страницы
        var CurrentPluginVersion = oAbout.PluginVersion;
        if (typeof(CurrentPluginVersion) == "undefined")
            CurrentPluginVersion = oAbout.Version;

        //document.getElementById('PluginEnabledImg').setAttribute("src", "Img/green_dot.png");
        //document.getElementById('PlugInEnabledTxt').innerHTML = "Плагин загружен.";
        //document.getElementById('PlugInVersionTxt').innerHTML = "Версия плагина: " + MakeVersionString(CurrentPluginVersion);
//        console.log("Плагин загружен.");
//        console.log("Версия плагина: " + MakeVersionString(CurrentPluginVersion));

        ShowCSPVersion_NPAPI(CurrentPluginVersion);
    }
    catch (err) {
        // Объект создать не удалось, проверим, установлен ли
        // вообще плагин. Такая возможность есть не во всех браузерах
        var mimetype = navigator.mimeTypes["application/x-cades"];
        if (mimetype) {
            isPluginLoaded = true;
            var plugin = mimetype.enabledPlugin;
            if (plugin) {
                isPluginEnabled = true;
            }
        }
    }
    /*
     GetLatestVersion_NPAPI(CurrentPluginVersion);
     if(location.pathname.indexOf("symalgo_sample.html")>=0){
     FillCertList_NPAPI('CertListBox1');
     FillCertList_NPAPI('CertListBox2');
     } else{
     FillCertList_NPAPI('CertListBox');
     }
     */

    FillCertList_NPAPI('CertListBox');
}

function CertificateObj(certObj)
{
    this.cert = certObj;
    this.certFromDate = new Date(this.cert.ValidFromDate);
    this.certTillDate = new Date(this.cert.ValidToDate);
}

CertificateObj.prototype.check = function(digit)
{
    return (digit<10) ? "0"+digit : digit;
}

CertificateObj.prototype.extract = function(from, what)
{
    certName = "";

    var begin = from.indexOf(what);

    if(begin>=0)
    {
        var end = from.indexOf(', ', begin);
        certName = (end<0) ? from.substr(begin) : from.substr(begin, end - begin);
    }

    return certName;
}

CertificateObj.prototype.DateTimePutTogether = function(certDate)
{
    return this.check(certDate.getUTCDate())+"."+this.check(certDate.getMonth()+1)+"."+certDate.getFullYear() + " " +
                 this.check(certDate.getUTCHours()) + ":" + this.check(certDate.getUTCMinutes()) + ":" + this.check(certDate.getUTCSeconds());
}

CertificateObj.prototype.GetCertString = function()
{
    return this.extract(this.cert.SubjectName,'CN=') + "; Выдан: " + this.GetCertFromDate();
}

CertificateObj.prototype.GetCertFromDate = function()
{
    return this.DateTimePutTogether(this.certFromDate);
}

CertificateObj.prototype.GetCertTillDate = function()
{
    return this.DateTimePutTogether(this.certTillDate);
}

CertificateObj.prototype.GetPubKeyAlgorithm = function()
{
    return this.cert.PublicKey().Algorithm.FriendlyName;
}

CertificateObj.prototype.GetCertName = function()
{
    return this.extract(this.cert.SubjectName, 'CN=');
}

CertificateObj.prototype.GetIssuer = function()
{
    return this.extract(this.cert.IssuerName, 'CN=');
}

CertificateObj.prototype.GetPrivateKeyProviderName = function()
{
    return this.cert.PrivateKey.ProviderName;
}

function GetFirstCert_NPAPI() {
    try {
        var oStore = cadesplugin.CreateObject("CAdESCOM.Store");
        oStore.Open();
    }
    catch (e) {
        alert("Ошибка при открытии хранилища: " + cadesplugin.getLastError(e));
        return;
    }

    var dateObj = new Date();
    var certCnt;

    try {
        certCnt = oStore.Certificates.Count;
        if(certCnt==0)
            throw "Cannot find object or property. (0x80092004)";
    }
    catch (ex) {
        var message = cadesplugin.getLastError(ex);
        if("Cannot find object or property. (0x80092004)" == message ||
           "oStore.Certificates is undefined" == message ||
           "Объект или свойство не найдено. (0x80092004)" == message)
        {
            oStore.Close();
            var errormes = document.getElementById("boxdiv").style.display = '';
            return;
        }
    }

    if(certCnt) {
        try {
            for (var i = 1; i <= certCnt; i++) {
                var cert = oStore.Certificates.Item(i);
                if(dateObj<cert.ValidToDate && cert.HasPrivateKey() && cert.IsValid().Result){
                    return cert;
                }
            }
        }
        catch (ex) {
            alert("Ошибка при перечислении сертификатов: " + cadesplugin.getLastError(ex));
            return;
        }
    }
}

function CreateSimpleSign_NPAPI()
{
    oCert = GetFirstCert_NPAPI();
    var x = GetSignatureTitleElement();
    try
    {
        if (typeof oCert != "undefined") {
            FillCertInfo_NPAPI(oCert);
            var sSignedData = MakeCadesBesSign_NPAPI(txtDataToSign, oCert);
            document.getElementById("SignatureTxtBox").innerHTML = sSignedData;
            if(x!=null)
            {
                x.innerHTML = "Подпись сформирована успешно:";
            }
        }
    }
    catch(err)
    {
        if(x!=null)
        {
            x.innerHTML = "Возникла ошибка:";
        }
        document.getElementById("SignatureTxtBox").innerHTML = err;
    }
}

function FillCertList_NPAPI(lstId) {

    try {
        var oStore = cadesplugin.CreateObject("CAdESCOM.Store");
        oStore.Open();
    } catch (ex) {
        callback.initCompleteFailure("Ошибка при открытии хранилища: " + cadesplugin.getLastError(ex));
        return;
    }
/*
    try {
        var lst = document.getElementById(lstId);
        if(!lst)
            return;
    } catch (ex) {
        return;
    }
*/

    var certCnt;

    try {
        certCnt = oStore.Certificates.Count;
        if(certCnt==0) callback.initCompleteFailure("Cannot find object or property. (0x80092004)");
    } catch (ex) {
        oStore.Close();
        callback.initCompleteFailure("Cannot open store");
        return;
    }

    certList = [];
    certNameList = [];
    certMetaList = [];
    certThumbprintList = [];

    for (var i = 1; i <= certCnt; i++) {
        var cert;
        try {
            cert = oStore.Certificates.Item(i);
        } catch (ex) {
            callback.initCompleteFailure("Ошибка при перечислении сертификатов: " + cadesplugin.getLastError(ex));
            return;
        }

        //var oOpt = document.createElement("OPTION");
        var dateObj = new Date();
        try {

            var snilsArr = getSnilsArr(cert.SubjectName);

            if ((!snilsArr)||(snilsArr.length===0)) {

                continue;
            }

            if(dateObj<cert.ValidToDate && cert.HasPrivateKey() && cert.IsValid().Result) {

                certList[i - 1] = cert;
                var certObj = new CertificateObj(cert);

                certNameList.push(certObj.GetCertString());
                certThumbprintList.push(cert.Thumbprint);

                var snils = snilsArr[0];

                var snilsItems = snils.split('=');

                if (snilsItems.length === 2) {
                    snils = snilsItems[1];
                }

                var name = cert.SubjectName;

                var issuer = certObj.GetIssuer();

                var fromDate = certObj.GetCertFromDate();

                var tillDate = certObj.GetCertTillDate();

                var providerName = certObj.GetPrivateKeyProviderName();

                var algorithm = certObj.GetPubKeyAlgorithm();

                var metaData = new certMetaData(name, issuer, fromDate, tillDate, providerName, algorithm, snils);

                certMetaList.push(JSON.stringify(metaData));

            } else {
                continue;
            }
        } catch (ex) {
            alert("Ошибка при получении свойства SubjectName: " + cadesplugin.getLastError(ex));
        }

        try {
//            console.log("Thumbprint: " + cert.Thumbprint);
        } catch (ex) {
            alert("Ошибка при получении свойства Thumbprint: " + cadesplugin.getLastError(ex));
        }

        //lst.options.add(oOpt);
    }

    oStore.Close();

    callback.initCompleteSuccess(certNameList, certThumbprintList, certMetaList);
}

function CreateCertRequest_NPAPI()
{
    try {
        var PrivateKey = cadesplugin.CreateObject("X509Enrollment.CX509PrivateKey");
    }
    catch (e) {
        alert('Failed to create X509Enrollment.CX509PrivateKey: ' + cadesplugin.getLastError(e));
        return;
    }

    PrivateKey.ProviderName = "Crypto-Pro GOST R 34.10-2001 Cryptographic Service Provider";
    PrivateKey.ProviderType = 75;
    PrivateKey.KeySpec = 1; //XCN_AT_KEYEXCHANGE

    try {
        var CertificateRequestPkcs10 = cadesplugin.CreateObject("X509Enrollment.CX509CertificateRequestPkcs10");
    }
    catch (e) {
        alert('Failed to create X509Enrollment.CX509CertificateRequestPkcs10: ' + cadesplugin.getLastError(e));
        return;
    }

    CertificateRequestPkcs10.InitializeFromPrivateKey(0x1, PrivateKey, "");

    try {
        var DistinguishedName = cadesplugin.CreateObject("X509Enrollment.CX500DistinguishedName");
    }
    catch (e) {
        alert('Failed to create X509Enrollment.CX500DistinguishedName: ' + cadesplugin.getLastError(e));
        return;
    }

    var CommonName = "Test Certificate";
    DistinguishedName.Encode("CN=\""+CommonName.replace(/"/g, "\"\"")+"\";");

    CertificateRequestPkcs10.Subject = DistinguishedName;

    var KeyUsageExtension = cadesplugin.CreateObject("X509Enrollment.CX509ExtensionKeyUsage");
    var CERT_DATA_ENCIPHERMENT_KEY_USAGE = 0x10;
    var CERT_KEY_ENCIPHERMENT_KEY_USAGE = 0x20;
    var CERT_DIGITAL_SIGNATURE_KEY_USAGE = 0x80;
    var CERT_NON_REPUDIATION_KEY_USAGE = 0x40;

    KeyUsageExtension.InitializeEncode(
                CERT_KEY_ENCIPHERMENT_KEY_USAGE |
                CERT_DATA_ENCIPHERMENT_KEY_USAGE |
                CERT_DIGITAL_SIGNATURE_KEY_USAGE |
                CERT_NON_REPUDIATION_KEY_USAGE);

    CertificateRequestPkcs10.X509Extensions.Add(KeyUsageExtension);

    try {
        var Enroll = cadesplugin.CreateObject("X509Enrollment.CX509Enrollment");
    }
    catch (e) {
        alert('Failed to create X509Enrollment.CX509Enrollment: ' + cadesplugin.getLastError(e));
        return;
    }

    Enroll.InitializeFromRequest(CertificateRequestPkcs10);

    return Enroll.CreateRequest(0x1);
}

function RetrieveCertificate_NPAPI()
{
    var cert_req = CreateCertRequest_NPAPI();
    var params = 'CertRequest=' + encodeURIComponent(cert_req) +
                 '&Mode=' + encodeURIComponent('newreq') +
                 '&TargetStoreFlags=' + encodeURIComponent('0') +
                 '&SaveCert=' + encodeURIComponent('no');

    var xmlhttp = getXmlHttp();
    xmlhttp.open("POST", "https://www.cryptopro.ru/certsrv/certfnsh.asp", true);
    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    var response;
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4) {
            if(xmlhttp.status == 200) {
                response = xmlhttp.responseText;
                var cert_data = "";

                if(!isIE())
                {
                    var start = response.indexOf("var sPKCS7");
                    var end = response.indexOf("sPKCS7 += \"\"") + 13;
                    cert_data = response.substring(start, end);
                }
                else
                {
                    var start = response.indexOf("sPKCS7 & \"") + 9;
                    var end = response.indexOf("& vbNewLine\r\n\r\n</Script>");
                    cert_data = response.substring(start, end);
                    cert_data = cert_data.replace(new RegExp(" & vbNewLine",'g'),";");
                    cert_data = cert_data.replace(new RegExp("&",'g'),"+");
                    cert_data = "var sPKCS7=" + cert_data + ";";
                }

                eval(cert_data);

                try {
                    var Enroll = cadesplugin.CreateObject("X509Enrollment.CX509Enrollment");
                }
                catch (e) {
                    alert('Failed to create X509Enrollment.CX509Enrollment: ' + cadesplugin.getLastError(e));
                    return;
                }

                Enroll.Initialize(0x1);
                Enroll.InstallResponse(0, sPKCS7, 0x7, "");
                var errormes = document.getElementById("boxdiv").style.display = 'none';
                if(location.pathname.indexOf("simple")>=0) {
                    location.reload();
                }
                else if(location.pathname.indexOf("symalgo_sample.html")>=0){
                    FillCertList_NPAPI('CertListBox1');
                    FillCertList_NPAPI('CertListBox2');
                }
                else{
                    FillCertList_NPAPI('CertListBox');
                }
            }
        }
    }
    xmlhttp.send(params);
}

function Encrypt_NPAPI() {

    document.getElementById("DataEncryptedIV1").innerHTML = "";
    document.getElementById("DataEncryptedIV2").innerHTML = "";
    document.getElementById("DataEncryptedDiversData1").innerHTML = "";
    document.getElementById("DataEncryptedDiversData2").innerHTML = "";
    document.getElementById("DataEncryptedBox1").innerHTML = "";
    document.getElementById("DataEncryptedBox2").innerHTML = "";
    document.getElementById("DataEncryptedKey1").innerHTML = "";
    document.getElementById("DataEncryptedKey2").innerHTML = "";
    document.getElementById("DataDecryptedBox1").innerHTML = "";
    document.getElementById("DataDecryptedBox2").innerHTML = "";

    var certificate1 = GetCertificate_NPAPI('CertListBox1');
    if(typeof(certificate1) == 'undefined')
    {
        return;
    }
    var certificate2 = GetCertificate_NPAPI('CertListBox2');
    if(typeof(certificate2) == 'undefined')
    {
        return;
    }

    var dataToEncr1 = Base64.encode(document.getElementById("DataToEncrTxtBox1").value);
    var dataToEncr2 = Base64.encode(document.getElementById("DataToEncrTxtBox2").value);

    try
    {
        FillCertInfo_NPAPI(certificate1, 'cert_info1');
        FillCertInfo_NPAPI(certificate2, 'cert_info2');
        var errormes = "";

        try {
            var oSymAlgo = cadesplugin.CreateObject("cadescom.symmetricalgorithm");
        } catch (err) {
            errormes = "Failed to create cadescom.symmetricalgorithm: " + err;
            alert(errormes);
            throw errormes;
        }

        oSymAlgo.GenerateKey();

        var oSesKey1 = oSymAlgo.DiversifyKey();
        var oSesKey1DiversData = oSesKey1.DiversData;
        document.getElementById("DataEncryptedDiversData1").value = oSesKey1DiversData;
        var oSesKey1IV = oSesKey1.IV;
        document.getElementById("DataEncryptedIV1").value = oSesKey1IV;
        var EncryptedData1 = oSesKey1.Encrypt(dataToEncr1, 1);
        document.getElementById("DataEncryptedBox1").value = EncryptedData1;

        var oSesKey2 = oSymAlgo.DiversifyKey();
        var oSesKey2DiversData = oSesKey2.DiversData;
        document.getElementById("DataEncryptedDiversData2").value = oSesKey2DiversData;
        var oSesKey2IV = oSesKey2.IV;
        document.getElementById("DataEncryptedIV2").value = oSesKey2IV;
        var EncryptedData2 = oSesKey2.Encrypt(dataToEncr2, 1);
        document.getElementById("DataEncryptedBox2").value = EncryptedData2;

        var ExportedKey1 = oSymAlgo.ExportKey(certificate1);
        document.getElementById("DataEncryptedKey1").value = ExportedKey1;

        var ExportedKey2 = oSymAlgo.ExportKey(certificate2);
        document.getElementById("DataEncryptedKey2").value = ExportedKey2;

        alert("Данные зашифрованы успешно:");
    }
    catch(err)
    {
        alert("Ошибка при шифровании данных:" + err);
    }
}

function Decrypt_NPAPI(certListBoxId) {

    document.getElementById("DataDecryptedBox1").value = "";
    document.getElementById("DataDecryptedBox2").value = "";

    var certificate = GetCertificate_NPAPI(certListBoxId);
    if(typeof(certificate) == 'undefined')
    {
        return;
    }
    var dataToDecr1 = document.getElementById("DataEncryptedBox1").value;
    var dataToDecr2 = document.getElementById("DataEncryptedBox2").value;
    var field;
    if(certListBoxId == 'CertListBox1')
        field ="DataEncryptedKey1";
    else
        field ="DataEncryptedKey2";

    var EncryptedKey = document.getElementById(field).value;
    try
    {
        FillCertInfo_NPAPI(certificate, 'cert_info_decr');
        var errormes = "";

        try {
            var oSymAlgo = cadesplugin.CreateObject("cadescom.symmetricalgorithm");
        } catch (err) {
            errormes = "Failed to create cadescom.symmetricalgorithm: " + err;
            alert(errormes);
            throw errormes;
        }
        oSymAlgo.ImportKey(EncryptedKey, certificate);
        var oSesKey1DiversData = document.getElementById("DataEncryptedDiversData1").value;
        var oSesKey1IV = document.getElementById("DataEncryptedIV1").value;
        oSymAlgo.DiversData = oSesKey1DiversData;
        var oSesKey1 = oSymAlgo.DiversifyKey();
        oSesKey1.IV = oSesKey1IV;
        var EncryptedData1 = oSesKey1.Decrypt(dataToDecr1, 1);
        document.getElementById("DataDecryptedBox1").value = Base64.decode(EncryptedData1);
        var oSesKey2DiversData = document.getElementById("DataEncryptedDiversData2").value;
        var oSesKey2IV = document.getElementById("DataEncryptedIV2").value;
        oSymAlgo.DiversData = oSesKey2DiversData;
        var oSesKey2 = oSymAlgo.DiversifyKey();
        oSesKey2.IV = oSesKey2IV;
        var EncryptedData2 = oSesKey2.Decrypt(dataToDecr2, 1);
        document.getElementById("DataDecryptedBox2").value = Base64.decode(EncryptedData2);

        alert("Данные расшифрованы успешно:");
    }
    catch(err)
    {
        alert("Ошибка при шифровании данных:" + err);
    }
}

function isIE() {
    var retVal = (("Microsoft Internet Explorer" == navigator.appName) || // IE < 11
        navigator.userAgent.match(/Trident\/./i)); // IE 11
    return retVal;
}

//-----------------------------------
var Base64 = {


    _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",


    encode: function(input) {
            var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;

        input = Base64._utf8_encode(input);

        while (i < input.length) {

            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);

            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;

            if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                    enc4 = 64;
            }

            output = output + this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);

        }

        return output;
    },


    decode: function(input) {
            var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;

        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

        while (i < input.length) {

            enc1 = this._keyStr.indexOf(input.charAt(i++));
            enc2 = this._keyStr.indexOf(input.charAt(i++));
            enc3 = this._keyStr.indexOf(input.charAt(i++));
            enc4 = this._keyStr.indexOf(input.charAt(i++));

            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;

            output = output + String.fromCharCode(chr1);

            if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
            }

        }

        output = Base64._utf8_decode(output);

        return output;

    },

    _utf8_encode: function(string) {
            string = string.replace(/\r\n/g, "\n");
        var utftext = "";

        for (var n = 0; n < string.length; n++) {

            var c = string.charCodeAt(n);

            if (c < 128) {
                    utftext += String.fromCharCode(c);
            }
            else if ((c > 127) && (c < 2048)) {
                    utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                    utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }

        return utftext;
    },

    _utf8_decode: function(utftext) {
            var string = "";
        var i = 0;
        var c = c1 = c2 = 0;

        while (i < utftext.length) {

            c = utftext.charCodeAt(i);

            if (c < 128) {
                    string += String.fromCharCode(c);
                i++;
            }
            else if ((c > 191) && (c < 224)) {
                    c2 = utftext.charCodeAt(i + 1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            }
            else {
                    c2 = utftext.charCodeAt(i + 1);
                c3 = utftext.charCodeAt(i + 2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }

        }

        return string;
    }

}
var MakePayment = function(sum,date,to){
    return '<!PINPADFILE UTF8><N>Платежное поручение<V>500'
        + '<N>Сумма<V>' + sum
        + '<N>Дата<V>' + date
        + '<N>Получатель<V>' + to
        + '<N>Инн<V>102125125212'
        + '<N>КПП<V>1254521521'
        + '<N>Назначение платежа<V>За телематические услуги'
        + '<N>Банк получателя<V>Сбербанк'
        + '<N>БИК<V>5005825'
        + '<N>Номер счета получателя<V>1032221122214422'
        + '<N>Плательщик<V>ЗАО "Актив-софт"'
        + '<N>Банк плательщика<V>Банк ВТБ (открытое акционерное общество)'
        + '<N>БИК<V>044525187'
        + '<N>Номер счета плательщика<V>30101810700000000187';
};



function ShowPinPadelogin(){
    var loginvalue = document.getElementById('Login').value;
    var text = '<!PINPADFILE UTF8><N>Авторизация<V><N>Подтвердите авторизацию на сайте<V>'
                + 'cryptopro.ru'
                + '<N>Вход будет произведен с логином<V>' + loginvalue;
    Common_SignCadesBES('CertListBox',text, 1);
}
var ver_match = window.navigator.userAgent.match(/Firefox\/([0-9]+)\./);
if(ver_match){
    var ver = parseInt(ver_match[1]);
    /*if (ver < 52) {
//        console.log("Данная демо-страница поддерживает бераузер Firefox версии 52 и выше! Для тестирования более ранних версий браузера используйте страницу http://www.cryptopro.ru/sites/default/files/products/cades/demopage/cades_bes_sample_oldff.html");
        console.log("бераузер Firefox ниже версии 52");
    }*/
}
