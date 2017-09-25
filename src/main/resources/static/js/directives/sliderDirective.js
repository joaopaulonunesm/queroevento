angular.module('queroEventoApp').directive('slider', slider);
  
  slider.$inject = ['$document']
  
  function slider($document) {
    var directive = {
      restrict: 'AC', //Apply the directive through Class or Attribute
      scope: {
        events: '='
      },
      template: '<ul class="slides">'
        + '<li data-ng-repeat="event in events">'
          + '<img data-ng-src="{{event.route}}" style="opacity: 0.3">'
          + '<div class="caption {{event.align}}-align">'
           + '<h3>{{event.caption}}</h3>'
           + '<h5 class="light grey-text text-lighten-3">{{event.slogan}}</h5>'
          + '</div>'
        + '</li>'
      + '</ul>',
      link: linkFunc
    };
    
    return directive;
    
    function linkFunc(scope, elem, attr) {
      $document.ready(function(){
        elem.slider({
          full_width: false,
          indicators: true,
          transition: 1000,
          interval: 7000
        });
      });
    }
  }