$(function() {
    commonParamManager.init();
});

var commonParamManager = {
    init: function () {
        commonParamManager.getList();
    },
    getList: function () {
        $.getJSON("/admin/param/list/1",function(resp) {
           if (resp.code == 200) {
                var htmlArr = [];
                for (var i=0; i<resp.data.length; i++) {
                    var param = resp.data[i];
                    htmlArr.push("<tr>");
                    htmlArr.push("<td>"+(i+1)+"</td>");
                    htmlArr.push("<td>"+param.paramName+"</td>");
                    htmlArr.push("<td><a href='javascript:void(0)' class='param-value' data-name='"+param.paramName+"' data-type='text' data-pk='"+param.id+"' data-url='/admin/param/updateSysParam' data-title='输入参数值'>"+param.paramValue+"</a></td>");
                    htmlArr.push("<td>"+param.descr+"</td>");
                    htmlArr.push("</tr>");
                }

               $("#paramTable").find("tbody").html(htmlArr.join(""));
               commonParamManager.initTable();
           }else {
               swal("查询失败", resp.msg,"error");
           }
        });
    },
    initTable: function () {
        $('.param-value').editable();
    }
}