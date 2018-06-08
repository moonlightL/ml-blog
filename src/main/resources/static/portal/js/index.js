$(function () {
    var Counter = AV.Object.extend("Counter");
    showTime(Counter);
});

function showTime(Counter) {
    var query = new AV.Query(Counter);
    var entries = [];
    var $visitors = $(".leancloud_visitors");

    $visitors.each(function () {
        entries.push($(this).attr("id"));
    });

    query.containedIn('url', entries);
    query.find()
        .done(function (results) {
            var COUNT_CONTAINER_REF = '.leancloud-visitors-count';

            if (results.length === 0) {
                $visitors.find(COUNT_CONTAINER_REF).text(0 + " 次");
                return;
            }

            for (var i = 0; i < results.length; i++) {
                var item = results[i];
                var url = item.get('url');
                var time = item.get('time');
                var element = document.getElementById(url);

                $(element).find(COUNT_CONTAINER_REF).text(time);
            }

            for(var i = 0; i < entries.length; i++) {
                var url = entries[i];
                var element = document.getElementById(url);
                var countSpan = $(element).find(COUNT_CONTAINER_REF);
                if( countSpan.text() == '') {
                    countSpan.text(0);
                }
            }
        })
        .fail(function (object, error) {
            console.log("Error: " + error.code + " " + error.message);
        });
}