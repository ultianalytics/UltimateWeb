// Generated by CoffeeScript 1.7.1
'use strict';
angular.module('newBetaApp').factory('gameStats', function($q, allGames, playerStats) {
  var api, deferred, games, psApi;
  deferred = $q.defer();
  games = {};
  psApi = {};
  $q.all([allGames, playerStats]).then(function(response) {
    games = response[0];
    psApi = response[1];
    return deferred.resolve(api);
  });
  api = {};
  api.getFor = function(game) {
    var leaders, players, relevant, results;
    results = {};
    players = psApi.getFrom([game]);
    relevant = _(games).where({
      opponentName: game.opponentName
    });
    if (!_(relevant).isArray()) {
      relevant = [relevant];
    }
    results.record = _(relevant).countBy(function(game) {
      if (game.ours > game.theirs) {
        return 'wins';
      } else {
        return 'losses';
      }
    });
    _(results.record).defaults({
      wins: 0,
      losses: 0
    });
    leaders = {};
    _(['goals', 'assists', 'ds', 'throwaways', 'plusMinus']).each(function(type) {
      return leaders[type] = _(players).max(function(player) {
        return player.stats[type];
      });
    });
    results.leaders = leaders;
    return results;
  };
  return deferred.promise;
});