package foundation.omni.test.consensus

import foundation.omni.CurrencyID
import foundation.omni.consensus.ChestConsensusTool
import foundation.omni.consensus.OmniwalletConsensusTool
import foundation.omni.rpc.SmartPropertyListInfo
import spock.lang.Specification

import static foundation.omni.CurrencyID.MSC
import static foundation.omni.CurrencyID.MAID
import static foundation.omni.CurrencyID.TMSC

/**
 * Basic functional test for getting consensus data from Chest API
 */
class ChestServerSpec extends Specification {

    def "Can get block height"() {
        setup:
        ChestConsensusTool fetcher = new ChestConsensusTool(ChestConsensusTool.ChestHost_Live)

        when: "we get block height"
        def blockHeight = fetcher.currentBlockHeight()

        then: "it looks reasonable"
        blockHeight > 323000  // Greater than a relatively recent main-net block
    }

    def "Can get Chest consensus data (divisible)"() {
        setup:
        ChestConsensusTool fetcher = new ChestConsensusTool(ChestConsensusTool.ChestHost_Live)

        when: "we get data"
        def snapshot = fetcher.getConsensusSnapshot(MSC)

        then: "something is there"
        snapshot.currencyID == MSC
        snapshot.blockHeight > 323000  // Greater than a relatively recent main-net block
        snapshot.entries.size() >= 1
    }

    def "Can get Chest consensus data (indivisible)"() {
        setup:
        ChestConsensusTool fetcher = new ChestConsensusTool(ChestConsensusTool.ChestHost_Live)

        when: "we get data"
        def snapshot = fetcher.getConsensusSnapshot(MAID)

        then: "something is there"
        snapshot.currencyID == MAID
        snapshot.blockHeight > 323000  // Greater than a relatively recent main-net block
        snapshot.entries.size() >= 1
    }

    def "Can get Chest property list"() {
        setup:
        ChestConsensusTool fetcher = new ChestConsensusTool(ChestConsensusTool.ChestHost_Live)

        when: "we get data"
        def properties = fetcher.listProperties()

        then: "we get a list of size >= 2"
        properties != null
        properties.size() >= 2

        when: "we convert the list to a map"
        // This may be unnecessary if we can assume the property list is ordered by propertyid
        Map<CurrencyID, SmartPropertyListInfo> props = properties.collect { [it.propertyid, it] }.collectEntries()

        then: "MSC and TMSC are not returned in Chest property list"
        props[MSC] == null
        props[TMSC] == null

    }
}