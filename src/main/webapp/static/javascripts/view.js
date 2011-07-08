(function($){
    $(function(){
        $(".pjax a").pjax(".content");
        $(".content").bind({
            "end.pjax": function(_, options){
                $(".content").hide();
                if( options.clickedElement.hasClass("up") ) {
                    $(".content").show("slide", { "direction" : "left" });
                }else{
                    $(".content").show("slide", { "direction" : "right" });
                }
            }
        });
    });
})(jQuery);
