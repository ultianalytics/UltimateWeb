// Generated by CoffeeScript 1.7.1
'use strict';
angular.module('newBetaApp').directive('targetMap', function($parse) {
  return {
    template: '<div id="target-map"><svg></svg></div>',
    restrict: 'E',
    scope: {
      data: '='
    },
    link: function(scope, element, attrs) {
      var diameter, getColor, getText, height;
      getColor = function(action) {
        var color;
        return color = (function() {
          switch (false) {
            case action !== 'Drop':
              return '#c5007c';
            case action !== 'Catch':
              return '#949A27';
            case action !== 'Throwaway':
              return '#ff9400';
            case action !== 'Goal':
              return '#298020';
            default:
              return '#c75aba';
          }
        })();
      };
      getText = function(data) {
        var at, dr, dv, plural, text;
        at = data.actionType;
        dv = data.value;
        dr = data.receiver;
        plural = dv > 1;
        return text = (function() {
          switch (false) {
            case at !== 'Throwaway':
              return dv + (plural ? ' throwaways' : ' throwaway');
            case at !== 'Catch':
              return dv + (plural ? ' passes to ' : ' pass to ') + dr;
            case at !== 'Goal':
              return dv + (plural ? ' Goals to ' : ' Goal to ') + dr;
            case at !== 'Drop':
              return dv + (plural ? ' dropped passes to ' : ' dropped pass to ') + dr;
            default:
              return at + ', ' + dr;
          }
        })();
      };
      height = parseInt($(element.parent()).css('width')) + 150;
      diameter = height - 150;
      $(element.parent()).css('height', height);
      return scope.$watch('data', function(newData) {
        var bubble, color, format, node, svg, tooltip;
        if (newData) {
          d3.select('#target-map').select('svg').remove();
          format = d3.format(',d');
          color = d3.scale.category20c();
          tooltip = d3.select('#target-map').append('div').attr('class', 'target-mouseover-tooltip').text('a simple tooltip');
          bubble = d3.layout.pack().sort(null).size([diameter, height + 100]).padding(1.5);
          svg = d3.select('#target-map').append('svg').attr('width', diameter).attr('height', height + 100).attr('class', 'bubble');
          node = svg.selectAll('.node').data(bubble.nodes(newData).filter(function(d) {
            return !d.children;
          })).enter().append('g').attr('class', 'target-node').attr('transform', function(d) {
            return 'translate(' + d.x + ',' + d.y + ')';
          }).on('mouseover', function(d) {
            return tooltip.style('visibility', 'visible').text(getText(d));
          }).on('mousemove', function() {
            return tooltip.style('top', d3.event.y - 10 + 'px').style('left', d3.event.x + 10 + 'px');
          }).on('mouseout', function() {
            return tooltip.style('visibility', 'hidden');
          });
          node.append('circle').attr("r", function(d) {
            return d.r;
          }).style("fill", function(d) {
            return getColor(d.actionType, true);
          });
          node.append('text').attr('dy', '.5em').style('text-anchor', 'middle').text(function(d) {
            return d.receiver.substring(0, d.r / 3);
          });
          return d3.select(self.frameElement).style('height', diameter + 'px');
        }
      });
    }
  };
});
