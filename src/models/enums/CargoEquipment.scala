package models.enums

object CargoEquipment extends Enumeration {
  val 
      /** EFFECT - Allows x number of commodities to be carried */
      CARGO_POD,
      /** EFFECT - Allows x number of commodities to be carried */
      ENHANCED_CARGO_POD,
      /** EFFECT - Provides visibility of NxN grid around the fleet */
      RADAR,
      /** EFFECT - Provides visibility of NxN grid around the fleet */
      ENHANCED_RADAR,
      /** EFFECT - Provides visibility of NxN grid around the fleet */
      DEEP_SPACE_RADAR,
      /** EFFECT - Provides visibility of cloaked ships in NxN grid around the fleet */
      STEALTH_RADAR,
      /** EFFECT - Improves FirePower by 30% */
      BATTLE_COMPUTER,
      /** EFFECT - Active Ability. Use once per n turns to disable one enemy ship */
      COMPUTER_JAMMER,
      /** EFFECT - Passive Ability - Incoming attack might miss (Torpedoes, and Guns) X% */
      ELECTRONIC_COUNTER_MEASURES,
      /** EFFECT - Passive Ability - Incoming attack might miss (Torpedoes, and Guns) more X% */
      ADVANCED_ELECTRONIC_COUNTER_MEASURES,
      /** EFFECT - Passive Ability. Nullifies ECM Effect on Target Ship */
      ANTI_ELECTRONIC_COUNTER_MEASURES,
      /** EFFECT - Passive Ability. Ship is invisible unless at least one enemy ship has a STEALTH_RADAR. Lasts entire battle */
      COMBAT_CLOAKING, 
      /** EFFECT - Passive Ability. Fleet is invisible unless at least one enemy ship has a STEALTH_RADAR. 
       *  Allows first turn if enemy ship has no STEALTH_RADAR */
      FLEET_CLOAKING = Value
}
