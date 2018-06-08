$(function () {
    loginManager.init();
});

var loginManager = {
    init: function () {
        // 解决 window 嵌套
        if (window.top != window) {
            window.top.location.href = window.location.href;
        }

        $("#loginBtn").on("click",function () {
            loginManager.login();
        });

        $(document).on("keyup",function (e) {
            if (e.keyCode == 13) {
                loginManager.login();
            }
        });

        // 切换验证码
        $("#changeBtn").on("click",function () {
            $(this).attr("src","/admin/captcha.jpg");
        });
    },
    login: function () {
        var username = $("#username").val();
        var password = $("#password").val();
        var captcha = $("#captcha").val();

        if (!username) {
            swal("用户名不能为空", "","error");
            return;
        }

        if (!password) {
            swal("密码不能为空", "","error");
            return;
        }

        if (!captcha) {
            swal("验证码不能为空", "","error");
            return;
        }

        $.post("/admin/login",{"username":username,"password":password,"captcha": captcha},function (resp) {
            if (resp.code == 200) {
                window.location.href = resp.data;
            } else if (resp.code == 400) {
                swal("登录失败", resp.msg,"error");
            }else {
                $("#changeBtn").attr("src","/admin/captcha.jpg");
                swal("登录失败", resp.msg,"error");
            }
        },"json");
    }
}
