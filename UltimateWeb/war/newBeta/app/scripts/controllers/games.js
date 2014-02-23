// Generated by CoffeeScript 1.7.1
'use strict';
angular.module('newBetaApp').controller('GamesCtrl', function($scope, $q, $location, allGames, playerStats, gameStats) {
  var games, gsApi, openPoints, psApi, scope;
  games = null;
  gsApi = null;
  psApi = null;
  scope = $scope;
  scope.loading = true;
  $q.all([allGames, playerStats, gameStats]).then(function(responses) {
    games = responses[0];
    psApi = responses[1];
    gsApi = responses[2];
    if ($location.search() && _(games).has($location.search())) {
      scope.select(games[$location.search()]);
    } else {
      scope.select(_(games).max(function(game) {
        return game.msSinceEpoch;
      }));
    }
    return scope.loading = false;
  });
  allGames.then(function(games) {
    scope.games = games;
    return scope.sortedGames = _(games).toArray();
  });
  scope.isSelectedGame = function(game) {
    return game === scope.selectedGame;
  };
  scope.select = function(game) {
    $location.search(game.gameId);
    scope.selectedGame = game;
    return scope.gameStats = gsApi.getFor(game);
  };
  openPoints = {};
  scope.togglePoints = function(points, only) {
    if (only) {
      openPoints = {};
    }
    return _(_(points).pluck('$$hashKey')).each(function(id) {
      return openPoints[id] = !openPoints[id];
    });
  };
  return scope.isOpen = function(point) {
    return openPoints[point['$$hashKey']];
  };
});
