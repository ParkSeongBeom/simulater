/**
 * URL 호출 시 업체에서 사용하는 JS
 */
let speeidPayment = new function(){

    this.params = {}

    this.winOpen = function(reParams){
        this.init(reParams);
        this.winOpenApi(this.params);
        this.callMessage(reParams);
    }

    this.init = function(reParams){
        this.paramsSet(reParams);
    }

    this.winOpenApi = function(reParams){
        let apiUrl = 'http://localhost:8080/if/bill/url';
        // // P, PE (단건 , 정기)
        // let apiUrl = 'https://erp-api.speeid.kr/if/bill/url';
        // if(reParams.bill.bill_type === "M"){
        //     // 정기 등록 수정
        //     apiUrl = 'https://erp-api.speeid.kr/if/rp/manage';
        // }

        // let apiUrl = 'http://stg.paymint.co.kr:11200/if/bill/url';
        // if(reParams.bill.bill_type === "M"){
        //     apiUrl = 'http://stg.paymint.co.kr:11200/if/rp/manage';
        // }

        var xhr = new XMLHttpRequest(); //new로 생성.
        xhr.open('POST', apiUrl, true); //open(http메서드, 요청을 처리할 페이지의 url, 요청이 비동기로 처리될 것인지를 지정하는 불리언)
        xhr.setRequestHeader("Content-Type", "application/json");

        //응답
        xhr.onload = function () { //브라우저가 서버로부터 응답을 받으면 onload이벤트가 발생.
            if(xhr.readyState == 4){
                if (xhr.status == 200) { //xml객체의 status속성값을 검사하고 서버의 응답이 정상인지 확인
                    //서버의 실행 결과를 처리할 코드
                    let rq = JSON.parse(this.responseText);
                    console.log(rq);
                    window.open(rq.shortURL, "SPEEID", "width=553,height=800, scrollbars=yes, resizable=yes");
                }
            }
        }
        xhr.send(JSON.stringify(reParams)); //준비된 요청을 전달. 괄호 내에 서버에 전달될 추가 정보 전달할 수도 있음.
    }


    this.paramsSet = function(reParams){
        let bill_id_time = new Date().getTime();
        this.params = {
            apikey: reParams.apikey,
            member: reParams.member,
            merchant: reParams.merchant,
            customer: reParams.customer, // Option - 사용자 ID
            bill: {
                bill_id: reParams.bill.bill_id || new Date().getTime(),                                                               // Option 고유번호(time)
                product_nm: reParams.bill.product_nm || '',                                                                           // 상품명
                message: reParams.bill.message || '',                                                                                 // 안내 메세지
                member_nm: reParams.bill.member_nm || '',                                                                             // Option - 수신자
                price: reParams.bill.price ? reParams.bill.price.replace(/ /g,"") : '' ,                                              // 물품금액
                hash:  reParams.bill.hash || ( reParams.bill.price ? this.SHA256(bill_id_time + ',' + reParams.bill.price) : ''  ),   // Option 암호화(SHA256)
                expire_dt: this.getNextDay(),                                                                                         // 유효기간 - Default( 발급기준 24시간 )
                callbackURL: reParams.bill.callbackURL || 'http://stg.paymint.co.kr:10201/test.php',                                  // 승인통보 URL
                bill_type: reParams.bill.bill_type || '',                                                                             // Option - 결제종류( 1회-P, 정기-PE ) - Default( P )
                bill_desc: reParams.bill.bill_desc || '',                                                                             // Option - 제공기간
                request_pay_type: reParams.bill.request_pay_type || '',                                                               // Option - 노출되는 결제수단
            },
            appr_state: reParams.appr_state,
            callSuccess: function(){},
            callFail: function(){},
        }
    }

    this.callMessage = function(reParams){
        let _This = this;
        window.addEventListener("message", function (e) {
            if(e.data.paymintCode){
                if(e.data.paymintCode === 'success'){
                    if(reParams.callSuccess) reParams.callSuccess(e.data.success_rep)
                } else if(e.data.paymintCode === 'fail'){
                    if(reParams.callFail) reParams.callFail(e.data.fail_rep);
                }
            }
        });
    }

    this.getNextDay = function (){
        let nextDay = new Date().setDate(new Date().getDate() + 1);
        let date = new Date(nextDay);
        let year = date.getFullYear();
        let month = (1 + date.getMonth());
        month = month >= 10 ? month : '0' + month;
        let day = date.getDate();
        day = day >= 10 ? day : '0' + day;
        return year + '-' + month + '-' + day;
    }

    this.SHA256 = function (s){

        var chrsz   = 8;
        var hexcase = 0;

        function safe_add (x, y) {
            var lsw = (x & 0xFFFF) + (y & 0xFFFF);
            var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
            return (msw << 16) | (lsw & 0xFFFF);
        }

        function S (X, n) { return ( X >>> n ) | (X << (32 - n)); }
        function R (X, n) { return ( X >>> n ); }
        function Ch(x, y, z) { return ((x & y) ^ ((~x) & z)); }
        function Maj(x, y, z) { return ((x & y) ^ (x & z) ^ (y & z)); }
        function Sigma0256(x) { return (S(x, 2) ^ S(x, 13) ^ S(x, 22)); }
        function Sigma1256(x) { return (S(x, 6) ^ S(x, 11) ^ S(x, 25)); }
        function Gamma0256(x) { return (S(x, 7) ^ S(x, 18) ^ R(x, 3)); }
        function Gamma1256(x) { return (S(x, 17) ^ S(x, 19) ^ R(x, 10)); }

        function core_sha256 (m, l) {

            var K = new Array(0x428A2F98, 0x71374491, 0xB5C0FBCF, 0xE9B5DBA5, 0x3956C25B, 0x59F111F1,
                0x923F82A4, 0xAB1C5ED5, 0xD807AA98, 0x12835B01, 0x243185BE, 0x550C7DC3,
                0x72BE5D74, 0x80DEB1FE, 0x9BDC06A7, 0xC19BF174, 0xE49B69C1, 0xEFBE4786,
                0xFC19DC6, 0x240CA1CC, 0x2DE92C6F, 0x4A7484AA, 0x5CB0A9DC, 0x76F988DA,
                0x983E5152, 0xA831C66D, 0xB00327C8, 0xBF597FC7, 0xC6E00BF3, 0xD5A79147,
                0x6CA6351, 0x14292967, 0x27B70A85, 0x2E1B2138, 0x4D2C6DFC, 0x53380D13,
                0x650A7354, 0x766A0ABB, 0x81C2C92E, 0x92722C85, 0xA2BFE8A1, 0xA81A664B,
                0xC24B8B70, 0xC76C51A3, 0xD192E819, 0xD6990624, 0xF40E3585, 0x106AA070,
                0x19A4C116, 0x1E376C08, 0x2748774C, 0x34B0BCB5, 0x391C0CB3, 0x4ED8AA4A,
                0x5B9CCA4F, 0x682E6FF3, 0x748F82EE, 0x78A5636F, 0x84C87814, 0x8CC70208,
                0x90BEFFFA, 0xA4506CEB, 0xBEF9A3F7, 0xC67178F2);

            var HASH = new Array(0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A, 0x510E527F, 0x9B05688C, 0x1F83D9AB, 0x5BE0CD19);

            var W = new Array(64);
            var a, b, c, d, e, f, g, h, i, j;
            var T1, T2;

            m[l >> 5] |= 0x80 << (24 - l % 32);
            m[((l + 64 >> 9) << 4) + 15] = l;

            for ( var i = 0; i<m.length; i+=16 ) {
                a = HASH[0];
                b = HASH[1];
                c = HASH[2];
                d = HASH[3];
                e = HASH[4];
                f = HASH[5];
                g = HASH[6];
                h = HASH[7];

                for ( var j = 0; j<64; j++) {
                    if (j < 16) W[j] = m[j + i];
                    else W[j] = safe_add(safe_add(safe_add(Gamma1256(W[j - 2]), W[j - 7]), Gamma0256(W[j - 15])), W[j - 16]);

                    T1 = safe_add(safe_add(safe_add(safe_add(h, Sigma1256(e)), Ch(e, f, g)), K[j]), W[j]);
                    T2 = safe_add(Sigma0256(a), Maj(a, b, c));

                    h = g;
                    g = f;
                    f = e;
                    e = safe_add(d, T1);
                    d = c;
                    c = b;
                    b = a;
                    a = safe_add(T1, T2);
                }

                HASH[0] = safe_add(a, HASH[0]);
                HASH[1] = safe_add(b, HASH[1]);
                HASH[2] = safe_add(c, HASH[2]);
                HASH[3] = safe_add(d, HASH[3]);
                HASH[4] = safe_add(e, HASH[4]);
                HASH[5] = safe_add(f, HASH[5]);
                HASH[6] = safe_add(g, HASH[6]);
                HASH[7] = safe_add(h, HASH[7]);
            }
            return HASH;
        }

        function str2binb (str) {
            var bin = Array();
            var mask = (1 << chrsz) - 1;
            for(var i = 0; i < str.length * chrsz; i += chrsz) {
                bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (24 - i%32);
            }
            return bin;
        }

        function Utf8Encode(string) {
            string = string.replace(/\r\n/g,"\n");
            var utftext = "";

            for (var n = 0; n < string.length; n++) {

                var c = string.charCodeAt(n);

                if (c < 128) {
                    utftext += String.fromCharCode(c);
                }
                else if((c > 127) && (c < 2048)) {
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
        }

        function binb2hex (binarray) {
            var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
            var str = "";
            for(var i = 0; i < binarray.length * 4; i++) {
                str += hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8+4)) & 0xF) +
                    hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8  )) & 0xF);
            }
            return str;
        }

        s = Utf8Encode(s);
        return binb2hex(core_sha256(str2binb(s), s.length * chrsz));
    }
}