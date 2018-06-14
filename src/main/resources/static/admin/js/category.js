$(function() {
    categoryManager.init();
});

var categoryManager = {
    init: function () {
        categoryManager.getList();

        $("#showBtn").on("click",function () {
            $("#categoryForm").get(0).reset();
            $("input[type='hidden']").val("");
            $("#previewBtn").attr("href","javascript:void(0)");
            $("#saveUI").modal("show");
            // 创建文件上传组件
            categoryManager.createFileComponent();
        });

        $("#submitBtn").on("click",function () {
           $.post("/admin/category/save",$("#categoryForm").serialize(),function (resp) {
               if (resp.code == 200) {
                   $("#saveUI").modal("hide");
                   swal("保存成功", "","success");
                   categoryManager.getList();
               } else {
                   swal("保存失败", resp.msg,"error");
               }
           },"json");
        });

        $(".text-color").on("click",function () {
           var color = $(this).data("color");
           $("#color").val(color);
        });

    },
    getList: function (pageNum) {
        $.getJSON("/admin/category/list/" + (pageNum || 1),function(resp) {
            if (resp.code == 200) {
                var pageInfo = resp.data;
                if (pageInfo.list.length > 0) {
                    var htmlArr = [];
                    for (var i=0; i< pageInfo.list.length; i++) {
                        var category = pageInfo.list[i];
                        htmlArr.push("<tr>");
                        htmlArr.push("<td>"+(i+1)+"</td>");
                        htmlArr.push("<td>"+category.name+"</td>");
                        htmlArr.push("<td>"+category.descr+"</td>");
                        htmlArr.push("<td><span class='fa fa-circle "+category.color+"'></td>");
                        htmlArr.push("<td><img src='"+category.imgUrl+"' alt='' width='32' height='32'></td>");
                        htmlArr.push("<td>"+category.sort+"</td>");
                        htmlArr.push("<td class='actions'><button type='button' class='btn btn-info waves-effect m-b-5 edit' data-id='"+category.id+"'>编辑</button>");
                        htmlArr.push("  <button type='button' class='btn btn-danger waves-effect m-b-5 delete' data-id='"+category.id+"'>删除</button></td>");
                        htmlArr.push("</tr>");
                    }
                    $("#categoryTable").find("tbody").html(htmlArr.join(""));
                    categoryManager.setPageBar(pageInfo);
                    categoryManager.bindClick();
                } else {
                    $("#categoryTable").find("tbody").html("<tr><td colspan='6' align='center'>暂无数据</td></tr>");
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
            categoryManager.getList(pageInfo.pageNum - 1);
        });

        $("#nextBtn").on("click",function () {
            if (!pageInfo.hasNextPage) {
                return;
            }
            categoryManager.getList(pageInfo.pageNum + 1);
        });
    },
    bindClick: function () {
        $(".edit").on("click",function () {
            var id = $(this).data("id");
            $.getJSON("/admin/category/get/"+id,function (resp) {
                if (resp.code == 200) {

                    for(var key in resp.data) {
                        $("#" + key).val(resp.data[key]);
                    }

                    $("#saveUI").modal("show");
                    // 创建文件上传组件
                    categoryManager.createFileComponent();

                } else {
                    swal("查询失败", resp.msg,"error");
                }
            })
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
                $.post("/admin/category/delete/"+id,null,function (resp) {
                    if (resp.code == 200) {
                        swal("删除成功", "","success");
                        categoryManager.getList();
                    } else {
                        swal("删除失败", resp.msg,"error");
                    }
                },"json");
            });
        });
    },
    createFileComponent: function () {
        $("#fileContainer").html("<div class='dz-message needsclick' id='fileUpload' style='cursor: pointer'>点击此处上传文件(需要配置七牛云)</div>");
        
        $("#fileUpload").dropzone({
            url: "/admin/uploadfile", //上传地址
            method: "POST", //方式
            addRemoveLinks: true,
            maxFiles: 10,
            maxFilesize: 5,
            uploadMultiple: false,
            parallelUploads: 100,
            previewsContainer: false,
            acceptedFiles: ".jpg, .jpeg,.png",
            success: function(file, resp, e) {
                if (resp.code == 200) {
                    $("#imgUrl").val(resp.data);
                    $("#previewBtn").attr("href",resp.data);
                } else {
                    swal("文件上传失败", resp.msg,"error");
                    // 重新创建，否则需要刷新页面才能继续上传文件
                    categoryManager.createFileComponent();
                }
            }
        });
    }
}