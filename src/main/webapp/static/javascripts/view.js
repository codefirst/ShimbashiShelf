(function($){
    $(function(){
        function update(){
            $(".mkdir").bind("click",function(){
                $("#mkdir").dialog({ width: 350, height: 80});
            });

            $(".upload").bind("click",function(){
                $("#upload").dialog({ width: 350, height: 120});
            });
        }

        $(".pjax a").pjax(".content");
        $(".content").bind({
            "end.pjax": function(_, options){
                $(".content").hide();
                if( options.clickedElement.hasClass("up") ) {
                    $(".content").show("slide", { "direction" : "left" });
                }else{
                    $(".content").show("slide", { "direction" : "right" });
                }
                update();
            }
        });
        update();
    });
})(jQuery);
