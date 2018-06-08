$.ajaxSetup({
    dataType: "json",
    cache: false,
    xhrFields: { withCredentials: true },//设置后，请求会携带cookie
    crossDomain: true,
    complete: function(xhr) {
        if (xhr.responseJSON) {
            if (xhr.responseJSON.code == 302) {
                window.parent.location.href = xhr.responseJSON.msg;
            }
        }
    }
});