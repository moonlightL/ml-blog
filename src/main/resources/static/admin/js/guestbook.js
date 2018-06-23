$(function () {
    guestbookManager.init();
});

var guestbookManager = {
    delStatus : 0,
    init: function () {
        guestbookManager.getList();

        $("#replyBtn").on("click",function () {
            var parameter = {
                "content": $("#replyContent").val(),
                "guestbookId": $("#id").val(),
                "imgUrl": "/portal/images/guestbook_author.jpg"
            }
            var index = layer.load(1);
            $.post("/admin/guestbook/save",parameter,function (resp) {
                layer.close(index);
                if (resp.code == 200) {
                    $("#replyUI").modal("hide");
                    swal("保存成功", "","success");
                    guestbookManager.getList();
                } else {
                    swal("保存失败", resp.msg,"error");
                }
            },"json");
        });

        $(".delStatus").on("click",function () {
            guestbookManager.delStatus = $(this).val();
            guestbookManager.getList(1);
        });
    },
    getList: function (pageNum) {
        $.getJSON("/admin/guestbook/list/"+(pageNum || 1) + "/" + guestbookManager.delStatus,function (resp) {

            if (resp.code == 200) {
                var pageInfo = resp.data;
                if (pageInfo.list.length > 0) {
                    var htmlArr = [];
                    for (var i=0; i< pageInfo.list.length; i++) {
                        var guestbook = pageInfo.list[i];
                        htmlArr.push("<tr>");
                        htmlArr.push("<td>"+(i+1)+"</td>");
                        htmlArr.push("<td><a href='/guestbook/#"+guestbook.nickname+"' target='_blank'>"+guestbook.nickname+"</a></td>");
                        htmlArr.push("<td>"+(guestbook.email || '')+"</td>");
                        htmlArr.push("<td><img src='"+guestbook.imgUrl+"' alt='' width='32' height='32'></td>");
                        htmlArr.push("<td style='white-space: nowrap;text-overflow: ellipsis;overflow: hidden;'><p style='width: 400px;'>"+guestbook.content+"</p></td>");
                        htmlArr.push("<td>"+(guestbook.type == 1 ? '留言' : '回复')+"</td>");
                        htmlArr.push("<td><a title='"+guestbook.ip+"'>"+guestbook.ipAddr+"</a></td>");
                        htmlArr.push("<td>"+guestbook.createTime+"</td>")
                        htmlArr.push("<td class='actions'><button type='button' class='btn btn-info waves-effect m-b-5 reply' data-id='"+guestbook.id+"'>回复</button>");
                        htmlArr.push(" <button type='button' class='btn btn-danger waves-effect m-b-5 delete' data-id='"+guestbook.id+"'>删除</button></td>");
                        htmlArr.push("</tr>");
                    }
                    $("#guestbookTable").find("tbody").html(htmlArr.join(""));
                    guestbookManager.setPageBar(pageInfo);
                    guestbookManager.bindClick();
                } else {
                    $("#guestbookTable").find("tbody").html("<tr><td colspan='10' align='center'>暂无数据</td></tr>");
                    $("#pageBar").html("");
                }
            } else {
                swal("查询失败", resp.msg,"error");
            }
        });
    },
    setPageBar: function (pageInfo) {
        var html = "<div class='col-xs-7' id='totalCount'></div>";
        html += "<div class='col-xs-5'>";
        html += "<div class='btn-group pull-right'>";
        html += "<button type='button' class='btn btn-default waves-effect' id='preBtn'><i class='fa fa-chevron-left'></i></button>";
        html += "<button type='button' class='btn btn-default waves-effect' id='nextBtn'><i class='fa fa-chevron-right'></i></button>";
        html += "</div>";
        html += "</div>";
        $("#pageBar").html(html);
        $("#totalCount").text("当前第 "+ pageInfo.pageNum +"/" + pageInfo.pages + " 页 - 总共 " + pageInfo.total + " 条记录");

        $("#preBtn").on("click",function () {
            if (!pageInfo.hasPreviousPage) {
                return;
            }
            guestbookManager.getList(pageInfo.pageNum - 1);
        });

        $("#nextBtn").on("click",function () {
            if (!pageInfo.hasNextPage) {
                return;
            }
            guestbookManager.getList(pageInfo.pageNum + 1);
        });
    },
    bindClick: function () {

        $(".reply").on("click",function () {
            var id = $(this).data("id");
            $.getJSON("/admin/guestbook/get/"+id,function (resp) {
                if (resp.code == 200) {

                    for(var key in resp.data) {
                        $("#" + key).val(resp.data[key]);
                    }

                    $("#replyUI").modal("show");

                } else {
                    swal("查询失败", resp.msg,"error");
                }
            });
        });

        $(".delete").on("click",function () {
            var that = this;
            swal({
                title: "确定删除吗？",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: false
            },
            function(){
                var id = $(that).data("id");
                $.post("/admin/guestbook/delete/"+id,null,function (resp) {
                    if (resp.code == 200) {
                        swal("删除成功", "","success");
                        guestbookManager.getList();
                    } else {
                        swal("删除失败", resp.msg,"error");
                    }
                },"json");
            });
        });
    }

}
