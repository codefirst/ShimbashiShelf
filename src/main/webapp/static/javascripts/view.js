(function($){
    $(function(){
        $("#pjax ul a").pjax("#pjax");
        $("#pjax").bind({
            "start.pjax": function(_, options){
                if( options.clickedElement.hasClass("up") ) {
                    $("#pjax").css( "margin-left", "-500px" );
                } else {
                    $("#pjax").css( "margin-left", "500px" );
                }
            },
            "end.pjax": function(){
                $("#pjax").animate({marginLeft:'0px'},'fast');
            }
        });
    });
})(jQuery);
