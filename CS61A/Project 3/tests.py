'''Unit tests for Ants Vs. SomeBees (ants.py).'''

import unittest
import doctest
import os
import sys
import imp
from ucb import main
import ants


class AntTest(unittest.TestCase):

    def setUp(self):
        hive, layout = ants.Hive(ants.make_test_assault_plan()), ants.test_layout
        self.colony = ants.AntColony(None, hive, ants.ant_types(), layout)


class TestProblem2(AntTest):

    def test_food_costs(self):
        error_msg = 'Wrong food_cost for ant class'
        self.assertEqual(2, ants.HarvesterAnt.food_cost, error_msg)
        self.assertEqual(4, ants.ThrowerAnt.food_cost, error_msg)

    def test_harvester(self):
        error_msg = 'HarvesterAnt did not add one food'
        old_food = self.colony.food
        ants.HarvesterAnt().action(self.colony)
        self.assertEqual(old_food + 1, self.colony.food, error_msg)


class TestProblem3(AntTest):

    def test_connectedness(self):
        error_msg = 'Entrances not properly initialized'
        for entrance in self.colony.bee_entrances:
            cur_place = entrance
            while cur_place:
                self.assertIsNotNone(cur_place.entrance, msg=error_msg)
                cur_place = cur_place.exit


class TestProblemA4(AntTest):

    def test_water_deadliness(self):
        error_msg = 'Water does not kill non-watersafe Insects'
        test_ant = ants.HarvesterAnt()
        test_water = ants.Water('water_TestProblemA4_0')
        test_water.add_insect(test_ant)
        self.assertIsNot(test_ant, test_water.ant, msg=error_msg)
        self.assertEqual(0, test_ant.armor, msg=error_msg)

    def test_water_safety(self):
        error_msg = 'Water kills watersafe Insects'
        test_bee = ants.Bee(1)
        test_water = ants.Water('water_testProblemA4_0')
        test_water.add_insect(test_bee)
        self.assertIn(test_bee, test_water.bees, msg=error_msg)

    def test_water_inheritance(self):
        error_msg = "It seems Water.add_insect is not using inheritance!"
        old_add_insect = ants.Place.add_insect
        def new_add_insect(self, insect):
            raise NotImplementedError()
        ants.Place.add_insect = new_add_insect
        test_bee = ants.Bee(1)
        test_water = ants.Water('water_testProblemA4_0')
        failed = True
        try:
            test_water.add_insect(test_bee)
        except NotImplementedError:
            failed = False
        finally:
            ants.Place.add_insect = old_add_insect
        if failed:
            self.fail(msg=error_msg)

    def test_water_inheritance_ant(self):
        error_msg = "Make sure to place the ant before killing it!"
        old_add_insect = ants.Place.add_insect
        def new_add_insect(self, insect):
            raise NotImplementedError()
        ants.Place.add_insect = new_add_insect
        test_ant = ants.HarvesterAnt()
        test_water = ants.Water('water_testProblemA4_0')
        failed = True
        try:
            test_water.add_insect(test_ant)
        except NotImplementedError:
            failed = False
        finally:
            ants.Place.add_insect = old_add_insect
        if failed:
            self.fail(msg=error_msg)


class TestProblemA5(AntTest):

    def test_fire_parameters(self):
        fire = ants.FireAnt()
        self.assertEqual(4, ants.FireAnt.food_cost, 'FireAnt has wrong food cost')
        self.assertEqual(1, fire.armor, 'FireAnt has wrong armor value')

    def test_fire_damage(self):
        error_msg = 'FireAnt does the wrong amount of damage'
        place = self.colony.places['tunnel_0_0']
        bee = ants.Bee(5)
        place.add_insect(bee)
        place.add_insect(ants.FireAnt())
        bee.action(self.colony)
        self.assertEqual(2, bee.armor, error_msg)

    def test_fire_deadliness(self):
        error_msg = 'FireAnt does not damage all Bees in its Place'
        test_place = self.colony.places['tunnel_0_0']
        bee = ants.Bee(3)
        test_place.add_insect(bee)
        test_place.add_insect(ants.Bee(3))
        test_place.add_insect(ants.FireAnt())
        bee.action(self.colony)
        self.assertEqual(0, len(test_place.bees), error_msg)


class TestProblemB4(AntTest):

    def test_nearest_bee(self):
        error_msg = 'ThrowerAnt can\'t find the nearest bee.'
        ant = ants.ThrowerAnt()
        self.colony.places['tunnel_0_0'].add_insect(ant)
        near_bee = ants.Bee(2)
        self.colony.places['tunnel_0_3'].add_insect(near_bee)
        self.colony.places['tunnel_0_6'].add_insect(ants.Bee(2))
        hive = self.colony.hive
        self.assertIs(ant.nearest_bee(hive), near_bee, error_msg)
        ant.action(self.colony)
        self.assertEqual(1, near_bee.armor, error_msg)


    def test_nearest_bee_not_in_hive(self):
        error_msg = 'ThrowerAnt hit a Bee in the Hive'
        ant = ants.ThrowerAnt()
        self.colony.places['tunnel_0_0'].add_insect(ant)
        hive = self.colony.hive
        hive.add_insect(ants.Bee(2))
        self.assertIsNone(ant.nearest_bee(hive), error_msg)


class TestProblemB5(AntTest):

    def test_thrower_parameters(self):
        short_t = ants.ShortThrower()
        long_t = ants.LongThrower()
        self.assertEqual(3, ants.ShortThrower.food_cost, 'ShortThrower has wrong cost')
        self.assertEqual(3, ants.LongThrower.food_cost, 'LongThrower has wrong cost')
        self.assertEqual(1, short_t.armor, 'ShortThrower has wrong armor')
        self.assertEqual(1, long_t.armor, 'LongThrower has wrong armor')

    def test_inheritance(self):
        """Tests to see if the Long and Short Throwers are actually using the
        inherited action from the ThrowerAnt.
        """
        old_thrower_action = ants.ThrowerAnt.action
        old_throw_at = ants.ThrowerAnt.throw_at
        def new_thrower_action(self, colony):
            raise NotImplementedError()
        def new_throw_at(self, target):
            raise NotImplementedError()

        failed_long = 0
        try: # Test action
            ants.ThrowerAnt.action = new_thrower_action
            test_long = ants.LongThrower()
            test_long.action(self.colony)
        except NotImplementedError:
            failed_long += 1
        finally:
            ants.ThrowerAnt.action = old_thrower_action

        try: # Test throw_at
            ants.ThrowerAnt.throw_at = new_throw_at
            test_long = ants.LongThrower()
            test_bee = ants.Bee(1)
            test_long.throw_at(test_bee)
        except NotImplementedError:
            failed_long += 1
        finally:
            ants.ThrowerAnt.throw_at = old_throw_at

        if failed_long < 2:
            self.fail(msg="LongThrower is not using inheritance!")

        failed_short = 0
        try: # Test action
            ants.ThrowerAnt.action = new_thrower_action
            test_short = ants.ShortThrower()
            test_short.action(self.colony)
        except NotImplementedError:
            failed_short += 1
        finally:
            ants.ThrowerAnt.action = old_thrower_action

        try: # Test throw_at
            ants.ThrowerAnt.throw_at = new_throw_at
            test_short = ants.ShortThrower()
            test_bee = ants.Bee(1)
            test_short.throw_at(test_bee)
        except NotImplementedError:
            failed_short += 1
        finally:
            ants.ThrowerAnt.throw_at = old_throw_at

        if failed_short < 2:
            self.fail(msg="ShortThrower is not using inheritance!")

    def test_long(self):
        error_msg = 'LongThrower has the wrong range'
        ant = ants.LongThrower()
        self.colony.places['tunnel_0_0'].add_insect(ant)
        out_of_range, in_range = ants.Bee(2), ants.Bee(2)
        self.colony.places['tunnel_0_3'].add_insect(out_of_range)
        self.colony.places['tunnel_0_4'].add_insect(in_range)
        ant.action(self.colony)
        self.assertEqual(in_range.armor, 1, error_msg)
        self.assertEqual(out_of_range.armor, 2, error_msg)

    def test_short(self):
        error_msg = 'ShortThrower has the wrong range'
        ant = ants.ShortThrower()
        self.colony.places['tunnel_0_0'].add_insect(ant)
        out_of_range, in_range = ants.Bee(2), ants.Bee(2)
        self.colony.places['tunnel_0_3'].add_insect(out_of_range)
        self.colony.places['tunnel_0_2'].add_insect(in_range)
        ant.action(self.colony)
        self.assertEqual(in_range.armor, 1, error_msg)
        self.assertEqual(out_of_range.armor, 2, error_msg)


class TestProblemA6(AntTest):

    def test_wall(self):
        error_msg = 'WallAnt isn\'t parameterized quite right'
        self.assertEqual(4, ants.WallAnt().armor, error_msg)
        self.assertEqual(4, ants.WallAnt.food_cost, error_msg)


class TestProblemA7(AntTest):

    def test_ninja_parameters(self):
        ninja = ants.NinjaAnt()
        self.assertEqual(6, ants.NinjaAnt.food_cost, 'NinjaAnt has wrong cost')
        self.assertEqual(1, ninja.armor, 'NinjaAnt has wrong armor')

    def test_non_ninja_blocks(self):
        error_msg = "Non-NinjaAnt does not block bees"
        p0 = self.colony.places['tunnel_0_0']
        p1 = self.colony.places['tunnel_0_1']
        bee = ants.Bee(2)
        p1.add_insect(bee)
        p1.add_insect(ants.ThrowerAnt())
        bee.action(self.colony)
        self.assertIsNot(p0, bee.place, error_msg)
    def test_ninja_does_not_block(self):
        error_msg = 'NinjaAnt blocks bees'
        p0 = self.colony.places['tunnel_0_0']
        p1 = self.colony.places['tunnel_0_1']
        bee = ants.Bee(2)
        p1.add_insect(bee)
        p1.add_insect(ants.NinjaAnt())
        bee.action(self.colony)
        self.assertIs(p0, bee.place, error_msg)

    def test_ninja_deadliness(self):
        error_msg = 'NinjaAnt does not strike all bees in its place'
        test_place = self.colony.places['tunnel_0_0']
        for _ in range(3):
            test_place.add_insect(ants.Bee(1))
        ninja = ants.NinjaAnt()
        test_place.add_insect(ninja)
        ninja.action(self.colony)
        self.assertEqual(0, len(test_place.bees), error_msg)


class TestProblemB6(AntTest):

    def test_scuba_parameters(self):
        scuba = ants.ScubaThrower()
        self.assertEqual(5, ants.ScubaThrower.food_cost, 'ScubaThrower has wrong cost')
        self.assertEqual(1, scuba.armor, 'ScubaThrower has wrong armor')

    def test_inheritance(self):
        """Tests to see if the ScubaThrower is actually using the inherited
        action from the ThrowerAnt.
        """
        old_thrower_action = ants.ThrowerAnt.action
        def new_thrower_action(self, colony):
            raise NotImplementedError()
        old_throw_at = ants.ThrowerAnt.throw_at
        def new_throw_at(self, target):
            raise NotImplementedError()

        failed_scuba = 0
        try:
            ants.ThrowerAnt.action = new_thrower_action
            test_scuba = ants.ScubaThrower()
            test_scuba.action(self.colony)
        except NotImplementedError:
            failed_scuba += 1
        finally:
            ants.ThrowerAnt.action = old_thrower_action

        try:
            ants.ThrowerAnt.throw_at = new_throw_at
            test_scuba = ants.ScubaThrower()
            test_bee = ants.Bee(1)
            test_scuba.throw_at(test_bee)
        except NotImplementedError:
            failed_scuba += 1
        finally:
            ants.ThrowerAnt.throw_at = old_throw_at

        if failed_scuba < 2:
            self.fail(msg="ScubaThrower is not using inheritance!")

    def test_scuba(self):
        error_msg = 'ScubaThrower sank'
        water = ants.Water('water')
        ant = ants.ScubaThrower()
        water.add_insect(ant)
        self.assertIs(water, ant.place, error_msg)
        self.assertEqual(1, ant.armor, error_msg)

    def test_scuba_on_land(self):
        place1 = self.colony.places['tunnel_0_0']
        place2 = self.colony.places['tunnel_0_4']
        ant = ants.ScubaThrower()
        bee = ants.Bee(3)
        place1.add_insect(ant)
        place2.add_insect(bee)
        ant.action(self.colony)
        self.assertEqual(2, bee.armor, 'ScubaThrower doesn\'t throw on land')


class TestProblemB7(AntTest):

    def test_hungry_parameters(self):
        hungry = ants.HungryAnt()
        self.assertEqual(4, ants.HungryAnt.food_cost, 'HungryAnt has wrong cost')
        self.assertEqual(1, hungry.armor, 'HungryAnt has wrong armor')

    def test_hungry_eats_and_digests(self):
        hungry = ants.HungryAnt()
        super_bee, super_pal = ants.Bee(1000), ants.Bee(1)
        place = self.colony.places['tunnel_0_0']
        place.add_insect(hungry)
        place.add_insect(super_bee)
        hungry.action(self.colony)
        self.assertEqual(0, super_bee.armor, 'HungryAnt didn\'t eat')
        place.add_insect(super_pal)
        for _ in range(3):
            hungry.action(self.colony)
        self.assertEqual(1, super_pal.armor, 'HungryAnt didn\'t digest')
        hungry.action(self.colony)
        self.assertEqual(0, super_pal.armor, 'HungryAnt didn\'t eat again')


class TestProblem8(AntTest):

    def setUp(self):
        AntTest.setUp(self)
        self.place = ants.Place('TestProblem8')
        self.bodyguard = ants.BodyguardAnt()
        self.bodyguard2 = ants.BodyguardAnt()
        self.test_ant = ants.Ant()
        self.test_ant2 = ants.Ant()
        self.harvester = ants.HarvesterAnt()

    def test_bodyguard_parameters(self):
        bodyguard = ants.BodyguardAnt()
        self.assertEqual(4, ants.BodyguardAnt.food_cost, 'BodyguardAnt has wrong cost')
        self.assertEqual(2, bodyguard.armor, 'BodyguardAnt has wrong armor')

    def test_bodyguardant_starts_empty(self):
        error_msg = 'BodyguardAnt doesn\'t start off empty'
        self.assertIsNone(self.bodyguard.ant, error_msg)

    def test_contain_ant(self):
        error_msg = 'BodyguardAnt.contain_ant doesn\'t properly contain ants'
        self.bodyguard.contain_ant(self.test_ant)
        self.assertIs(self.bodyguard.ant, self.test_ant, error_msg)

    def test_bodyguardant_is_container(self):
        error_msg = 'BodyguardAnt isn\'t a container'
        self.assertTrue(self.bodyguard.container, error_msg)

    def test_ant_is_not_container(self):
        error_msg = 'Normal Ants are containers'
        self.assertFalse(self.test_ant.container, error_msg)

    def test_can_contain1(self):
        error_msg = 'can_contain returns False for container ants'
        self.assertTrue(self.bodyguard.can_contain(self.test_ant), error_msg)

    def test_can_contain2(self):
        error_msg = 'can_contain returns True for non-container ants'
        self.assertFalse(self.test_ant.can_contain(self.test_ant2), error_msg)

    def test_can_contain3(self):
        error_msg = 'can_contain lets container ants contain other containers'
        self.assertFalse(self.bodyguard.can_contain(self.bodyguard2), error_msg)

    def test_can_contain4(self):
        error_msg = 'can_contain lets container ants contain multiple ants'
        self.bodyguard.contain_ant(self.test_ant)
        self.assertFalse(self.bodyguard.can_contain(self.test_ant2), error_msg)

    def test_modified_add_insect1(self):
        error_msg = \
            'Place.add_insect doesn\'t place Ants on BodyguardAnts properly'
        self.place.add_insect(self.bodyguard)
        try:
            self.place.add_insect(self.test_ant)
        except:
            self.fail(error_msg)
        self.assertIs(self.bodyguard.ant, self.test_ant, error_msg)
        self.assertIs(self.place.ant, self.bodyguard, error_msg)

    def test_modified_add_insect2(self):
        error_msg = \
            'Place.add_insect doesn\'t place BodyguardAnts on Ants properly'
        self.place.add_insect(self.test_ant)
        try:
            self.place.add_insect(self.bodyguard)
        except:
            self.fail(error_msg)
        self.assertIs(self.bodyguard.ant, self.test_ant, error_msg)
        self.assertIs(self.place.ant, self.bodyguard, error_msg)

    def test_bodyguardant_perish(self):
        error_msg = \
            'BodyguardAnts aren\'t replaced with the contained Ant when perishing'
        self.place.add_insect(self.bodyguard)
        self.place.add_insect(self.test_ant)
        self.bodyguard.reduce_armor(self.bodyguard.armor)
        self.assertIs(self.place.ant, self.test_ant, error_msg)

    def test_bodyguardant_work(self):
        error_msg = 'BodyguardAnts don\'t let the contained ant do work'
        food = self.colony.food
        self.bodyguard.contain_ant(self.harvester)
        self.bodyguard.action(self.colony)
        self.assertEqual(food+1, self.colony.food, error_msg)

    def test_thrower(self):
        error_msg = 'ThrowerAnt can\'t throw from inside a bodyguard'
        ant = ants.ThrowerAnt()
        self.colony.places['tunnel_0_0'].add_insect(self.bodyguard)
        self.colony.places['tunnel_0_0'].add_insect(ant)
        bee = ants.Bee(2)
        self.colony.places['tunnel_0_3'].add_insect(bee)
        self.bodyguard.action(self.colony)
        self.assertEqual(1, bee.armor, error_msg)


class TestProblem9(AntTest):

    def setUp(self):
        imp.reload(ants)
        hive = ants.Hive(ants.make_test_assault_plan())
        layout = ants.test_layout_multi_tunnels
        self.colony = ants.AntColony(None, hive, ants.ant_types(), layout)
        self.princess_peach = ants.PrincessAnt()
        self.princess_daisy = ants.PrincessAnt()

    def test_boost(self):
        thrower = ants.ThrowerAnt()
        fire = ants.FireAnt()
        front = ants.ThrowerAnt()
        thrower_armor = thrower.armor
        fire_armor = fire.armor
        princess_armor = self.princess_peach.armor
        thrower_damage = thrower.damage
        self.colony.places['tunnel_0_0'].add_insect(thrower)
        self.colony.places['tunnel_0_1'].add_insect(fire)
        self.colony.places['tunnel_0_2'].add_insect(self.princess_peach)
        self.colony.places['tunnel_0_3'].add_insect(front)
        thrower.action(self.colony) # Armor boosted only after princess's action
        self.assertEqual(thrower_armor, thrower.armor,
                "Armor boosted too early")
        self.princess_peach.action(self.colony) # Princesses have normal armor
        self.assertEqual(self.princess_peach.armor, princess_armor,
                "Princess armor incorrect")
        front.action(self.colony) # Check if armor is boosted in front
        self.assertEqual(front.armor, thrower_armor+1,
                "Did not boost ant in front of princess correctly")
        fire.action(self.colony) # Check if armor is boosted in back
        self.assertEqual(fire.armor, fire_armor+1,
                "Did not boost ant behind princess correctly")
        thrower.action(self.colony) # Check if armor is boosted way back
        self.assertEqual(thrower.armor, thrower_armor+1,
                "Did not boost ant behind ant behind princess correctly")
        self.assertEqual(thrower.damage, thrower_damage, "Damage incorrect")

    def test_boost_after_death(self):
        bee = ants.Bee(5)
        thrower = ants.ThrowerAnt()
        thrower_armor = thrower.armor
        self.colony.places['tunnel_0_0'].add_insect(self.princess_daisy)
        self.colony.places['tunnel_0_1'].add_insect(thrower)
        self.princess_daisy.action(self.colony)
        self.colony.places['tunnel_0_0'].add_insect(bee)
        bee.action(self.colony)
        self.assertEqual(thrower.armor, thrower_armor+1,
            "Other ants' armors should remain boosted after princess expires")

    def test_double_boost(self):
        thrower = ants.ThrowerAnt()
        thrower_armor = thrower.armor
        princess_armor = self.princess_peach.armor
        self.colony.places['tunnel_0_0'].add_insect(self.princess_peach)
        self.colony.places['tunnel_0_1'].add_insect(thrower)
        self.colony.places['tunnel_0_2'].add_insect(self.princess_daisy)
        self.princess_peach.action(self.colony)
        self.princess_daisy.action(self.colony)
        self.assertEqual(thrower.armor, thrower_armor+2,
                "Other ant's armor should have been boosted twice")
        self.assertEqual(self.princess_peach.armor, princess_armor+1,
                "Princess armor should have been boosted once")
        self.assertEqual(self.princess_daisy.armor, princess_armor+1,
                "Princess armor should have been boosted once")

    def test_multi_tunnel(self):
        thrower0 = ants.ThrowerAnt()
        thrower1 = ants.ThrowerAnt()
        thrower_armor = thrower0.armor
        self.colony.places['tunnel_0_0'].add_insect(thrower0)
        self.colony.places['tunnel_0_1'].add_insect(self.princess_peach)
        self.colony.places['tunnel_1_0'].add_insect(thrower1)
        self.princess_peach.action(self.colony)
        self.assertEqual(thrower0.armor, thrower_armor+1,
                "Armor for ant in princess's tunnel incorrectly boosted")
        self.assertEqual(thrower1.armor, thrower_armor,
                "Armor for ant not in princess's tunnel incorrectly boosted")

    def test_bodyguarded_princess(self):
        bodyguard = ants.BodyguardAnt()
        bodyguard_armor = bodyguard.armor
        princess_armor = self.princess_peach.armor
        self.colony.places['tunnel_0_0'].add_insect(self.princess_peach)
        self.colony.places['tunnel_0_0'].add_insect(bodyguard)
        self.princess_peach.action(self.colony)
        bodyguard.action(self.colony)
        self.assertEqual(bodyguard.armor, bodyguard_armor+1,
                "Armor for BodyguardAnt incorrectly boosted")
        self.assertEqual(self.princess_peach.armor, princess_armor,
                "Armor for protected princess should not be boosted")

    def test_double_bodyguarded_princesses(self):
        peach_bodyguard = ants.BodyguardAnt()
        daisy_bodyguard = ants.BodyguardAnt()
        princess_armor = self.princess_peach.armor
        bodyguard_armor = peach_bodyguard.armor
        self.colony.places['tunnel_0_0'].add_insect(self.princess_peach)
        self.colony.places['tunnel_0_0'].add_insect(peach_bodyguard)
        self.colony.places['tunnel_0_1'].add_insect(self.princess_daisy)
        self.colony.places['tunnel_0_1'].add_insect(daisy_bodyguard)
        self.princess_peach.action(self.colony)
        self.princess_daisy.action(self.colony)
        self.assertEqual(peach_bodyguard.armor, bodyguard_armor+2,
                "Armor for BodyguardAnt incorrectly boosted")
        self.assertEqual(daisy_bodyguard.armor, bodyguard_armor+2,
                "Armor for BodyguardAnt incorrectly boosted")
        self.assertEqual(self.princess_peach.armor, princess_armor,
                "Armor for protected princess should not be boosted")
        self.assertEqual(self.princess_daisy.armor, princess_armor,
                "Armor for protected princess should not be boosted")

class TestExtraCredit(AntTest):

    def test_status_parameters(self):
        slow = ants.SlowThrower()
        stun = ants.StunThrower()
        self.assertEqual(4, ants.SlowThrower.food_cost, 'SlowThrower has wrong cost')
        self.assertEqual(6, ants.StunThrower.food_cost, 'StunThrower has wrong cost')
        self.assertEqual(1, slow.armor, 'SlowThrower has wrong armor')
        self.assertEqual(1, stun.armor, 'StunThrower has wrong armor')

    def test_slow(self):
        error_msg = 'SlowThrower doesn\'t cause slowness on odd turns.'
        slow = ants.SlowThrower()
        bee = ants.Bee(3)
        self.colony.places['tunnel_0_0'].add_insect(slow)
        self.colony.places['tunnel_0_4'].add_insect(bee)
        slow.action(self.colony)
        self.colony.time = 1
        bee.action(self.colony)
        self.assertEqual('tunnel_0_4', bee.place.name, error_msg)
        self.colony.time += 1
        bee.action(self.colony)
        self.assertEqual('tunnel_0_3', bee.place.name, error_msg)
        for _ in range(3):
            self.colony.time += 1
            bee.action(self.colony)
        self.assertEqual('tunnel_0_1', bee.place.name, error_msg)

    def test_stun(self):
        error_msg = 'StunThrower doesn\'t stun for exactly one turn.'
        stun = ants.StunThrower()
        bee = ants.Bee(3)
        self.colony.places['tunnel_0_0'].add_insect(stun)
        self.colony.places['tunnel_0_4'].add_insect(bee)
        stun.action(self.colony)
        bee.action(self.colony)
        self.assertEqual('tunnel_0_4', bee.place.name, error_msg)
        bee.action(self.colony)
        self.assertEqual('tunnel_0_3', bee.place.name, error_msg)

    def test_effect_stack(self):
        stun = ants.StunThrower()
        bee = ants.Bee(3)
        stun_place = self.colony.places['tunnel_0_0']
        bee_place = self.colony.places['tunnel_0_4']
        stun_place.add_insect(stun)
        bee_place.add_insect(bee)
        for _ in range(4): # stun bee four times
            stun.action(self.colony)
        for _ in range(4):
            bee.action(self.colony)
            self.assertEqual('tunnel_0_4', bee.place.name,
                             'Status effects do not stack')

    def test_multiple_stuns(self):
        error_msg = "2 StunThrowers stunning 2 Bees doesn't stun each Bee for exactly one turn."
        stun1 = ants.StunThrower()
        stun2 = ants.StunThrower()
        bee1 = ants.Bee(3)
        bee2 = ants.Bee(3)

        self.colony.places['tunnel_0_0'].add_insect(stun1)
        self.colony.places['tunnel_0_1'].add_insect(bee1)
        self.colony.places['tunnel_0_2'].add_insect(stun2)
        self.colony.places['tunnel_0_3'].add_insect(bee2)

        stun1.action(self.colony)
        stun2.action(self.colony)
        bee1.action(self.colony)
        bee2.action(self.colony)

        self.assertEqual('tunnel_0_1', bee1.place.name, error_msg)
        self.assertEqual('tunnel_0_3', bee2.place.name, error_msg)

        bee1.action(self.colony)
        bee2.action(self.colony)

        self.assertEqual('tunnel_0_0', bee1.place.name, error_msg)
        self.assertEqual('tunnel_0_2', bee2.place.name, error_msg)

    def test_long_effect_stack(self):
        stun = ants.StunThrower()
        slow = ants.SlowThrower()
        bee = ants.Bee(3)
        self.colony.places['tunnel_0_0'].add_insect(stun)
        self.colony.places['tunnel_0_1'].add_insect(slow)
        self.colony.places['tunnel_0_4'].add_insect(bee)
        for _ in range(3): # slow bee three times
            slow.action(self.colony)
        stun.action(self.colony) # stun bee once

        self.colony.time = 0
        bee.action(self.colony) # stunned
        self.assertEqual('tunnel_0_4', bee.place.name)

        self.colony.time = 1
        bee.action(self.colony) # slowed thrice
        self.assertEqual('tunnel_0_4', bee.place.name)

        self.colony.time = 2
        bee.action(self.colony) # slowed thrice
        self.assertEqual('tunnel_0_3', bee.place.name)

        self.colony.time = 3
        bee.action(self.colony) # slowed thrice
        self.assertEqual('tunnel_0_3', bee.place.name)

        self.colony.time = 4
        bee.action(self.colony) # slowed twice
        self.assertEqual('tunnel_0_2', bee.place.name)

        self.colony.time = 5
        bee.action(self.colony) # slowed twice
        self.assertEqual('tunnel_0_2', bee.place.name)

        self.colony.time = 6
        bee.action(self.colony) # slowed once
        self.assertEqual('tunnel_0_1', bee.place.name)

        self.colony.time = 7
        bee.action(self.colony) # no effects
        self.assertEqual(0, slow.armor)

@main
def main(*args):
    import argparse
    parser = argparse.ArgumentParser(description="Run Ants Tests")
    parser.add_argument('--verbose', '-v', action='store_true')
    args = parser.parse_args()
    doctest.testmod(ants, verbose=args.verbose)
    stdout = sys.stdout
    with open(os.devnull, 'w') as sys.stdout:
        verbosity = 2 if args.verbose else 1
        tests = unittest.main(exit=False, verbosity=verbosity)
    sys.stdout = stdout
