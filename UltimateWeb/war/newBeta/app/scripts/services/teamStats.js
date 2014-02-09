/* global _*/

'use strict';

angular.module('newBetaApp')
  .factory('teamStats', function($q, $routeParams, $rootScope, filter, api, allGames) {
    var deferred = $q.defer();
    var statsMap = {};
    var collection = 0;
    var goal;
    allGames.then(function(games){
      goal = _.keys(games).length;
      _.each(games, function(game, id){
        api.retrieveTeamStatsForGames($routeParams.teamId,[id],
          function success(result) {
            statsMap[id] = result;
            if (++collection === goal){
              deferred.resolve(tsApi);
            }
          },
          function failure(e) {
            deferred.reject(e);
          }
        );
      });
    });
    var tsApi = {
      getFromIncluded: function(){
        var that = this;
        var result = {};
        var temp = $rootScope.$new();
        temp.included = filter.included;
        temp.$watchCollection('included', function(){
          _(result).extend(that.getFrom(filter.included));
        });
        return result;
      },
      getFrom: function(games){
        var result = {};

        // Record
        var record = {wins:0,losses:0};
        _(games).each(function(game){
          var gs = statsMap[game.gameId].goalSummary;
          gs.ourOlineGoals + gs.ourDlineGoals > gs.theirOlineGoals + gs.theirDlineGoals ? record.wins++ : record.losses++;
        });
        result.record = record;

        // Point Spread
        var ps = {ours:0, theirs: 0};
        _(games).each(function(game){
          var gs = statsMap[game.gameId].goalSummary;
          ps.ours += gs.ourDlineGoals + gs.ourOlineGoals;
          ps.theirs += gs.theirDlineGoals + gs.theirOlineGoals;
        });
        result.pointSpread = ps;

        // Offensive Conversion
        var offensiveOpps = 0;
        var offensiveConversions = 0;
        _(games).each(function(game){
          _(game.points).each(function(point){
            if (point.summary.lineType === 'O'){
              offensiveOpps++;
              if (point.events[point.events.length-1].type === 'Offense'){
                offensiveConversions++;
              }
            }
          });
        });
        result.offensiveProductivity = offensiveOpps ? Math.round(offensiveConversions / offensiveOpps * 100): 0;

        // Conversion Rate

        var scoringOpps = 0;
        _(games).each(function(game){
          _(game.points).each(function(point){
            if (point.summary.lineType === 'O') {scoringOpps++;}
            _(point.events).each(function(event){
              if ((event.action === 'D' && event.type === 'Defense') || (event.action === 'Throwaway' && event.type === 'Defense')){
                scoringOpps++;
              }
            });
          });
        });
        result.conversionRate = scoringOpps ? Math.round(result.pointSpread.ours / scoringOpps * 100) : 0;

        // Throws per possession
        var scored = [];
        var failed = [];
        var passes = 0;
        _(games).each(function(game){
          _(game.points).each(function(point){
            passes = 0;
            _(point.events).each(function(event){
              if (event.type === 'Offense'){
                if (event.action === 'Catch'){
                  passes++;
                } else if(event.action ==='Goal'){
                  scored.push(++passes);
                  passes = 0;
                } else if(event.action === 'Throwaway' || event.action === 'Drop' || event.action === 'Turnover' || event.action === 'Stall'){
                  failed.push(++passes);
                  passes = 0;
                }
              } else {
                if (passes > 0){
                  scored.push(passes);
                  passes = 0;
                }
              }
            });
          });
        });
        var tpp = {};
        var groupingFunc = function(num){
          if (num === 1) {return '1';}
          if (num < 4) {return '4';}
          if (num < 8) {return '8';}
          if (num < 12) {return '12';}
          return '12+';
        };
        tpp.scored = _(scored).countBy(groupingFunc);
        tpp.failed = _(failed).countBy(groupingFunc);
        result.throwsPerPossession = tpp;
        return result;
      }
    };
    return deferred.promise;
  });
