insert ignore into users(username, password, active)
values("admin", "{noop}admin", 1)
;

insert ignore into roles(username, role)
values("admin", "ROLE_ADMIN")
;

insert ignore into uaap_game_codes(game_code, game_name)
values("MBB", "Men's Basketball")
, ("WBB", "Women's Basketball")
, ("MVB", "Men's Volleyball")
, ("WVB", "Women's Volleyball")
;

insert ignore into uaap_seasons(id, game_code, season_number, url)
values("87-MBB", "MBB", 87, "https://uaap.livestats.ph/tournaments/uaap-season-87-men-s?game_id=:id" )
, ("87-WBB", "WBB", 87, "https://uaap.livestats.ph/tournaments/uaap-season-87-men-s?game_id=:id" )
, ("86-MVB", "MVB", 86, "https://uaapvolleyball.livestats.ph/tournaments/uaap-volleyball-season-86-men-s?game_id=:id" )
, ("86-WVB", "WVB", 86, "https://uaapvolleyball.livestats.ph/tournaments/uaap-volleyball-season-86-women-s?game_id=:id" )
;