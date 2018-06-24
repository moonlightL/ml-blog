$(function () {
    var Counter = AV.Object.extend("Counter");
    addCount(Counter);
});

function addCount(Counter) {
    var $visitors = $(".leancloud_visitors");
    var url = $visitors.attr('id').trim();
    var title = $visitors.attr('data-flag-title').trim();
    var query = new AV.Query(Counter);

    query.equalTo("url", url);
    query.find({
        success: function(results) {
            if (results.length > 0) {
                var counter = results[0];
                counter.fetchWhenSave(true);
                counter.increment("time");
                counter.save(null, {
                    success: function(counter) {
                        var $element = $(document.getElementById(url));
                        $element.find('.leancloud-visitors-count').text(counter.get('time'));
                    },
                    error: function(counter, error) {
                        console.log('Failed to save Visitor num, with error message: ' + error.message);
                    }
                });
            } else {
                var newcounter = new Counter();
                /* Set ACL */
                var acl = new AV.ACL();
                acl.setPublicReadAccess(true);
                acl.setPublicWriteAccess(true);
                newcounter.setACL(acl);
                /* End Set ACL */
                newcounter.set("title", title);
                newcounter.set("url", url);
                newcounter.set("time", 1);
                newcounter.save(null, {
                    success: function(newcounter) {
                        var $element = $(document.getElementById(url));
                        $element.find('.leancloud-visitors-count').text(newcounter.get('time'));
                    },
                    error: function(newcounter, error) {
                        console.log('Failed to create');
                    }
                });
            }
        },
        error: function(error) {
            console.log('Error:' + error.code + " " + error.message);
        }
    });
}
