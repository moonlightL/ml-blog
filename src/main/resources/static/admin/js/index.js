$(function () {

    indexManger.init();
});

var indexManger = {
    init: function () {
        indexManger.indexData();
        indexManger.sysParamData();
        indexManger.sysLogData();
        indexManger.registerUpdatePwd();
        indexManger.registerLogout();
        indexManger.registerLog();
    },
    indexData: function () {
        $.getJSON("/admin/index/indexData",function (resp) {
            if (resp.code == 200) {
                for (var key in resp.data) {
                    $("#" + key).text(resp.data[key]);
                }
            } else {
                swal("指标数据查询失败", resp.msg,"error");
            }
        });
    },
    sysParamData: function () {
        $.getJSON("/admin/index/sysParamData",function (resp) {
            if (resp.code == 200) {
                var htmlArr = [];

                for(var i=0; i<resp.data.length; i++) {
                    var param = resp.data[i];
                    htmlArr.push("<tr>");
                    htmlArr.push("<td>"+param.descr+"</td>");
                    htmlArr.push("<td>"+param.paramValue+"</td>");
                    htmlArr.push("</tr>");
                }

                $("#paramTable").find("tbody").html(htmlArr.join(""));
            } else {
                swal("系统参数查询失败", resp.msg,"error");
            }
        });
    },
    sysLogData: function () {
        $.getJSON("/admin/index/sysLogData",function (resp) {
            if (resp.code == 200) {

                if (resp.data.length > 0) {
                    var htmlArr = [];
                    for (var i=0; i<resp.data.length; i++) {
                        var log = resp.data[i];
                        htmlArr.push("<a href='javascript:void(0)'>");
                        htmlArr.push("<div class='inbox-item'>");
                        htmlArr.push(" <div class='inbox-item-img'><img src='assets/images/users/avatar-1.jpg' class='img-circle' alt=''></div>");
                        htmlArr.push("<p class='inbox-item-author'>"+log.operator+"</p>");
                        htmlArr.push("<p class='inbox-item-text'>"+ log.method + " (" + log.descr+")</p>");
                        htmlArr.push("<p class='inbox-item-date'>"+log.createTime+"</p>");
                        htmlArr.push("</div>");
                        htmlArr.push("</a>");
                    }

                    $("#logContainer").html(htmlArr.join(""));
                } else {
                    $("#logContainer").html("<div style='text-align: center'>暂无数据</div>");
                }

            } else {
                swal("操作日志查询失败", resp.msg,"error");
            }
        });
    },
    registerLogout: function () {
        $("#logoutBtn").on("click",function () {
            swal({
                title: "确定注销吗？",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: false
            },
            function(){
                $.getJSON("/admin/logout",function (resp) {
                    if (resp.code == 200) {
                       window.location.href = resp.data;
                    } else {
                        swal("注销失败", resp.msg,"error");
                    }
                });
            });
        });
    },
    registerUpdatePwd: function () {
        $("#resetPwdBtn").on("click",function () {
            $("#updateForm").get(0).reset();
            $("#saveUI").modal("show");
        });

        $("#submitBtn").on("click",function () {

            var oldpwd = $("#oldpwd").val();
            var newpwd = $("#newpwd").val();
            var confirmpwd = $("#confirmpwd").val();
            if (newpwd != confirmpwd) {
                swal("新密码和确认密码不一致", "","error");
                return;
            }

           $.post("/admin/updatePwd",{"oldpwd": oldpwd,"newpwd": newpwd},function(resp) {
               if (resp.code == 200) {
                   swal("修改密码成功,需要重新登录", "","success");
                   $("#saveUI").modal("hide");
               } else {
                   swal("修改密码失败", resp.msg,"error");
               }
           },"json");
        });
    },
    registerLog: function () {
        $("#clearBtn").on("click",function () {
            swal({
                title: "确定要清空日志吗？",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: false
            },
            function(){

                $.post("/admin/index/clearLogData",null,function(resp) {
                    if (resp.code == 200) {
                        window.location.reload(true);
                    } else {
                        swal("清空失败", resp.msg,"error");
                    }
                },"json");
            });
        });
    }
}