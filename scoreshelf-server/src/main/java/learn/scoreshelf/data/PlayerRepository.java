package learn.scoreshelf.data;

import learn.scoreshelf.models.Player;

import java.util.List;

public interface PlayerRepository {

    List<Player> findAll();

    Player findById(int playerId);

    List<Player> findByAppUserId(int appUserId);

    Player add(Player player);

    boolean update(Player player);

    boolean deleteById(int playerId);
}