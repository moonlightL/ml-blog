$(function () {

    postManager.init();
});

var postManager = {
    myEditor: null,
    categoryId: 0,
    title: "",
    init: function () {
        postManager.getList();
        postManager.getCategoryList();
        postManager.registerEvent();
    },
    getList: function (pageNum) {
        $.getJSON("/admin/post/list/"+postManager.categoryId+"/"+ (pageNum || 1),{"title":postManager.title},function (resp) {
            if (resp.code == 200) {
                var pageInfo = resp.data;
                if (pageInfo.list.length > 0) {
                    var htmlArr = [];
                    for (var i=0; i< pageInfo.list.length; i++) {
                        var post = pageInfo.list[i];

                        htmlArr.push("<tr>");
                        htmlArr.push("<td class='mail-select'><div class='checkbox checkbox-primary'><input id='checkbox_"+post.id+"' type='checkbox'><label for='checkbox_"+post.id+"'></label> </div></td>");
                        htmlArr.push("<td><a href='/"+post.postUrl+"' target='_blank'>"+post.title+"</a></td> ");
                        htmlArr.push("<td>"+post.categoryName+"</td>");
                        htmlArr.push("<td>"+(post.tags || '')+"</td>");
                        htmlArr.push("<td>"+(post.status == 1 ? '显示' : '隐藏')+"</td>");
                        htmlArr.push("<td><img src='/portal/images/random/"+post.imgUrl+"' width='30' height='30' alt=''></td> ");
                        htmlArr.push("<td>"+(post.publishDate || '')+"</td>");
                        htmlArr.push("<td>"+post.createTime+"</td>");
                        htmlArr.push("<td class='actions'><button type='button' class='btn btn-info waves-effect m-b-5 edit' data-id='"+post.id+"'>编辑</button>");
                        htmlArr.push("  <button type='button' class='btn btn-danger waves-effect m-b-5 delete' data-id='"+post.id+"'>删除</button></td>");
                        htmlArr.push("</tr>");

                    }
                    $("#postTable").find("tbody").html(htmlArr.join(""));
                    postManager.setPageBar(pageInfo);
                    postManager.bindClick();
                } else {
                    $("#postTable").find("tbody").html("<tr><td colspan='9' align='center'>暂无数据</td></tr>");
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
            postManager.getList(pageInfo.pageNum - 1);
        });

        $("#nextBtn").on("click",function () {
            if (!pageInfo.hasNextPage) {
                return;
            }
            postManager.getList(pageInfo.pageNum + 1);
        });
    },
    bindClick: function () {
        $(".edit").on("click",function () {
            var id = $(this).data("id");
            $.getJSON("/admin/post/get/"+id,function (resp) {
                if (resp.code == 200) {

                    for(var key in resp.data) {
                        $("#" + key).val(resp.data[key]);
                    }

                    postManager.createEditor(function() {
                        var post = resp.data;
                        for(var key in post) {

                            if(key == "tags") {
                                $("#tags").tagsinput('add', post[key]);
                            } else if(key == "status") {
                                $("#showStatus").prop('checked', post[key] == 1).change()
                            } else {
                                $("#" + key).val(post[key]);
                            }

                        }
                        postManager.myEditor.setMarkdown(post.content);
                    });

                    $("#saveUI").modal("show");
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
                    $.post("/admin/post/delete/"+id,null,function (resp) {
                        if (resp.code == 200) {
                            swal("删除成功", "","success");
                            postManager.getList();
                        } else {
                            swal("删除失败", resp.msg,"error");
                        }
                    },"json");
                });
        });
    },
    registerEvent: function () {

        $("#checkboxAll").on("click",function () {
            $("#postTable").find("input[type='checkbox']").prop("checked",$(this).prop("checked"));
        });

        $("#deletesBtn").on("click",function () {
            var idArr = [];
            $("#postTable").find("tbody").find("input[type='checkbox']:checked").each(function (index,domEle) {
                var id = $(domEle).attr("id").split("_")[1];
                idArr.push(id);
            });

            if (idArr.length > 0) {
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
                        $.post("/admin/post/deleteBatch/"+idArr.join(","),null,function (resp) {
                            if (resp.code == 200) {
                                swal("删除成功", "","success");
                                $("#checkboxAll").prop("checked",false);
                                postManager.getList();
                            } else {
                                swal("删除失败", resp.msg,"error");
                            }
                        },"json");
                    });
            } else {
                swal("删除失败", "请勾选文章后进行删除","error");
            }
        });

        $("#showBtn").on("click",function () {

            $("#saveUI").modal("show");

            postManager.createEditor(function() {
                $("#postForm").get(0).reset();
                $("#tags").tagsinput('removeAll');
                $("input[type='hidden']").val("");
            });
        });

        $("#submitBtn").on("click",function () {
            var parameter = {
                "id": $("#id").val(),
                "title": $("#title").val(),
                "keyword": $("#keyword").val(),
                "categoryId": $("#categoryId").val(),
                "tags": $("#tags").val(),
                "status": ($("#showStatus").prop("checked") ? 1: 0 ),
                "content": postManager.myEditor.getMarkdown()
            };

            $.post("/admin/post/save",parameter,function (resp) {
                if (resp.code == 200) {
                    $("#saveUI").modal("hide");
                    swal("保存成功", "","success");
                    postManager.getList();
                } else {
                    swal("保存失败", resp.msg,"error");
                }
            },"json");
        });

        $("#importUIBtn").on("click",function () {
            $("#path").val("");
            $("#importUI").modal("show");
        });

        // md 文件导入事件
        $("#importBtn").on("click",function () {
            var that = this;
            var path = $("#path").val();
            if (!path) {
                swal("导入目录不能为空", "","error");
                return;
            }
            var index = layer.load(1);
            $(that).prop("disabled",true);
           $.post("/admin/post/importFiles",{"path": path},function (resp) {
               layer.close(index);
               $(that).prop("disabled",false);
               if (resp.code == 200) {
                   $("#importUI").modal("hide");
                   swal("导入成功", "","success");
                   window.location.reload(true);
               } else {
                   swal("导入失败", resp.msg,"error");
               }
           },"json");
        });

        // 查询事件
        $("#queryBtn").on("click",function () {
            postManager.title = $("#query").val();
            postManager.getList();
        });
    },
    getCategoryList: function () {
        $.getJSON("/admin/category/listAll",function (resp) {
           if (resp.code == 200) {
               var htmlArr = ["<a href='javascript:void(0)' class='list-group-item no-border category active' data-id='0'><span class='fa fa-circle text-default pull-right'></span>全部</a>"];

               for (var i=0; i<resp.data.length; i++) {
                   var category = resp.data[i];
                   htmlArr.push("<a href='javascript:void(0)' class='list-group-item no-border category' data-id='"+category.id+"'><span class='fa fa-circle "+category.color+" pull-right'></span>"+category.name+"</a>");
               }

               $("#categoryType").html(htmlArr.join(""));

               var htmlArr2 = ["<option value=''>--请选择--</option>"];
               for (var j=0; j<resp.data.length; j++) {
                   var category2 = resp.data[j];
                   htmlArr2.push("<option value='"+category2.id+"'>"+category2.name+"</option>");
               }

               $("#categoryId").html(htmlArr2.join(""));

               $(".category").on("click",function () {
                  if ($(this).hasClass("active") ) {
                      return;
                  }

                  $(this).parent().children().removeClass("active");
                  $(this).addClass("active");
                  postManager.categoryId = $(this).data("id");
                  postManager.getList();

               });

           } else {
               swal("查询失败", resp.msg,"error");
           }
        });
    },
    createEditor: function (callback) {
        $("#editorContainer").html("<div class='col-lg-12' id='my-editormd'></div>");
        postManager.myEditor = editormd("my-editormd", {
            width   : "100%",
            height  :  $("#saveUI").height() - 350,
            syncScrolling : "single",
            path    : "/admin/assets/plugins/editormd/lib/",
            placeholder: "写点内容吧(配置七牛云后可在该编辑器上实现图片上传)~~",
            toolbarIcons : function() {
                return ["undo","redo","bold","del","italic","quote","ucwords","uppercase","lowercase","h1","h2","h3","h4","h5","h6","list-ul","list-ol","hr","link","reference-link","image","code","preformatted-text","code-block","table","datetime","pagebreak","watch","unwatch","preview","clear","search"]
            },
            codeFold : true,
            imageUpload : true,
            imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
            imageUploadURL : "/admin/mdUploadfile",
            onload: function () {
                callback();
            }
        });
    }
}