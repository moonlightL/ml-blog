$(function () {
    aboutMeManager.init();
});

var aboutMeManager = {
    myEditor: null,
    init: function () {
        aboutMeManager.getContent();

        $("#submitBtn").on("click",function () {
            var parameter = {
                "id": $("#id").val(),
                "status": ($("#showStatus").prop("checked") ? 1: 0 ),
                "content": aboutMeManager.myEditor.getMarkdown()
            };
            $.post("/admin/aboutMe/save",parameter,function (resp) {
                if (resp.code == 200) {
                    swal("保存成功", "","success");
                } else {
                    swal("保存失败", resp.msg,"error");
                }
            },"json");
        });
    },
    getContent: function () {

        $.getJSON("/admin/aboutMe/getAboutMe",function (resp) {
            if (resp.code == 200) {
                aboutMeManager.createEditor(function() {
                    var aboutMe = resp.data;
                    $("#id").val(aboutMe.id);
                    $("#showStatus").prop("checked",aboutMe.status == 1).change();
                    aboutMeManager.myEditor.setMarkdown(aboutMe.content);
                });
            } else {
                swal("查询失败", resp.msg,"error");
            }
        });

    },
    createEditor: function (callback) {
        $("#editorContainer").html("<div class='col-lg-12' id='my-editormd'></div>");
        aboutMeManager.myEditor = editormd("my-editormd", {
            width   : "100%",
            height  :  500,
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