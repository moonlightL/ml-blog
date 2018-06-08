$(function () {
    personParamManager.init();
});

var personParamManager = {
    init: function () {
        personParamManager.getPersonParamData();
        personParamManager.bindClick();
    },
    getPersonParamData: function () {
        $.getJSON("/admin/param/list/2",function (resp) {
            if (resp.code == 200) {
                if(resp.data.length > 0) {
                    for(var i=0; i<resp.data.length; i++) {
                        var param = resp.data[i];
                        $("#" + param.paramName).val(param.paramValue);
                    }
                }
            } else {
                swal("查询失败", resp.msg,"error");
            }
        });
    },
    bindClick: function () {
        $(".submit").on("click",function () {

            var parameter = $(this).parents("form").serialize();

            $.post("/admin/param/updatePerParam",parameter,function (resp) {
                if (resp.code == 200) {
                    swal("保存成功","","success");
                } else {
                    swal("保存失败", resp.msg,"error");
                }
            },"json");

        });
    }
}