
package org.durgaveg.com.bo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sourceInterval",
    "quoteSourceName",
    "regularMarketOpen",
    "exchange",
    "regularMarketTime",
    "fiftyTwoWeekRange",
    "sharesOutstanding",
    "regularMarketDayHigh",
    "shortName",
    "longName",
    "exchangeTimezoneName",
    "regularMarketChange",
    "regularMarketPreviousClose",
    "fiftyTwoWeekHighChange",
    "exchangeTimezoneShortName",
    "fiftyTwoWeekLowChange",
    "exchangeDataDelayedBy",
    "regularMarketDayLow",
    "priceHint",
    "currency",
    "regularMarketPrice",
    "regularMarketVolume",
    "isLoading",
    "gmtOffSetMilliseconds",
    "marketState",
    "marketCap",
    "quoteType",
    "invalid",
    "symbol",
    "language",
    "fiftyTwoWeekLowChangePercent",
    "regularMarketDayRange",
    "messageBoardId",
    "fiftyTwoWeekHigh",
    "fiftyTwoWeekHighChangePercent",
    "uuid",
    "market",
    "fiftyTwoWeekLow",
    "regularMarketChangePercent",
    "fullExchangeName",
    "tradeable"
})
public class QuoteData implements Serializable
{

    /**
     * The Sourceinterval Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("sourceInterval")
    private Integer sourceInterval = 0;
    /**
     * The Quotesourcename Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("quoteSourceName")
    private String quoteSourceName = "";
    @JsonProperty("regularMarketOpen")
    private RegularMarketOpen regularMarketOpen;
    /**
     * The Exchange Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchange")
    private String exchange = "";
    @JsonProperty("regularMarketTime")
    private RegularMarketTime regularMarketTime;
    @JsonProperty("fiftyTwoWeekRange")
    private FiftyTwoWeekRange fiftyTwoWeekRange;
    @JsonProperty("sharesOutstanding")
    private SharesOutstanding sharesOutstanding;
    @JsonProperty("regularMarketDayHigh")
    private RegularMarketDayHigh regularMarketDayHigh;
    /**
     * The Shortname Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("shortName")
    private String shortName = "";
    /**
     * The Longname Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("longName")
    private String longName = "";
    /**
     * The Exchangetimezonename Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchangeTimezoneName")
    private String exchangeTimezoneName = "";
    @JsonProperty("regularMarketChange")
    private RegularMarketChange regularMarketChange;
    @JsonProperty("regularMarketPreviousClose")
    private RegularMarketPreviousClose regularMarketPreviousClose;
    @JsonProperty("fiftyTwoWeekHighChange")
    private FiftyTwoWeekHighChange fiftyTwoWeekHighChange;
    /**
     * The Exchangetimezoneshortname Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchangeTimezoneShortName")
    private String exchangeTimezoneShortName = "";
    @JsonProperty("fiftyTwoWeekLowChange")
    private FiftyTwoWeekLowChange fiftyTwoWeekLowChange;
    /**
     * The Exchangedatadelayedby Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchangeDataDelayedBy")
    private Integer exchangeDataDelayedBy = 0;
    @JsonProperty("regularMarketDayLow")
    private RegularMarketDayLow regularMarketDayLow;
    /**
     * The Pricehint Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("priceHint")
    private Integer priceHint = 0;
    /**
     * The Currency Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("currency")
    private String currency = "";
    @JsonProperty("regularMarketPrice")
    private RegularMarketPrice regularMarketPrice;
    @JsonProperty("regularMarketVolume")
    private RegularMarketVolume regularMarketVolume;
    /**
     * The Isloading Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("isLoading")
    private Boolean isLoading = false;
    /**
     * The Gmtoffsetmilliseconds Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("gmtOffSetMilliseconds")
    private Integer gmtOffSetMilliseconds = 0;
    /**
     * The Marketstate Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("marketState")
    private String marketState = "";
    @JsonProperty("marketCap")
    private MarketCap marketCap;
    /**
     * The Quotetype Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("quoteType")
    private String quoteType = "";
    /**
     * The Invalid Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("invalid")
    private Boolean invalid = false;
    /**
     * The Symbol Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("symbol")
    private String symbol = "";
    /**
     * The Language Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("language")
    private String language = "";
    @JsonProperty("fiftyTwoWeekLowChangePercent")
    private FiftyTwoWeekLowChangePercent fiftyTwoWeekLowChangePercent;
    @JsonProperty("regularMarketDayRange")
    private RegularMarketDayRange regularMarketDayRange;
    /**
     * The Messageboardid Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("messageBoardId")
    private String messageBoardId = "";
    @JsonProperty("fiftyTwoWeekHigh")
    private FiftyTwoWeekHigh fiftyTwoWeekHigh;
    @JsonProperty("fiftyTwoWeekHighChangePercent")
    private FiftyTwoWeekHighChangePercent fiftyTwoWeekHighChangePercent;
    /**
     * The Uuid Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("uuid")
    private String uuid = "";
    /**
     * The Market Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("market")
    private String market = "";
    @JsonProperty("fiftyTwoWeekLow")
    private FiftyTwoWeekLow fiftyTwoWeekLow;
    @JsonProperty("regularMarketChangePercent")
    private RegularMarketChangePercent regularMarketChangePercent;
    /**
     * The Fullexchangename Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("fullExchangeName")
    private String fullExchangeName = "";
    /**
     * The Tradeable Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("tradeable")
    private Boolean tradeable = false;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1014233374513196734L;

    /**
     * The Sourceinterval Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("sourceInterval")
    public Integer getSourceInterval() {
        return sourceInterval;
    }

    /**
     * The Sourceinterval Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("sourceInterval")
    public void setSourceInterval(Integer sourceInterval) {
        this.sourceInterval = sourceInterval;
    }

    /**
     * The Quotesourcename Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("quoteSourceName")
    public String getQuoteSourceName() {
        return quoteSourceName;
    }

    /**
     * The Quotesourcename Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("quoteSourceName")
    public void setQuoteSourceName(String quoteSourceName) {
        this.quoteSourceName = quoteSourceName;
    }

    @JsonProperty("regularMarketOpen")
    public RegularMarketOpen getRegularMarketOpen() {
        return regularMarketOpen;
    }

    @JsonProperty("regularMarketOpen")
    public void setRegularMarketOpen(RegularMarketOpen regularMarketOpen) {
        this.regularMarketOpen = regularMarketOpen;
    }

    /**
     * The Exchange Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchange")
    public String getExchange() {
        return exchange;
    }

    /**
     * The Exchange Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchange")
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @JsonProperty("regularMarketTime")
    public RegularMarketTime getRegularMarketTime() {
        return regularMarketTime;
    }

    @JsonProperty("regularMarketTime")
    public void setRegularMarketTime(RegularMarketTime regularMarketTime) {
        this.regularMarketTime = regularMarketTime;
    }

    @JsonProperty("fiftyTwoWeekRange")
    public FiftyTwoWeekRange getFiftyTwoWeekRange() {
        return fiftyTwoWeekRange;
    }

    @JsonProperty("fiftyTwoWeekRange")
    public void setFiftyTwoWeekRange(FiftyTwoWeekRange fiftyTwoWeekRange) {
        this.fiftyTwoWeekRange = fiftyTwoWeekRange;
    }

    @JsonProperty("sharesOutstanding")
    public SharesOutstanding getSharesOutstanding() {
        return sharesOutstanding;
    }

    @JsonProperty("sharesOutstanding")
    public void setSharesOutstanding(SharesOutstanding sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    @JsonProperty("regularMarketDayHigh")
    public RegularMarketDayHigh getRegularMarketDayHigh() {
        return regularMarketDayHigh;
    }

    @JsonProperty("regularMarketDayHigh")
    public void setRegularMarketDayHigh(RegularMarketDayHigh regularMarketDayHigh) {
        this.regularMarketDayHigh = regularMarketDayHigh;
    }

    /**
     * The Shortname Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("shortName")
    public String getShortName() {
        return shortName;
    }

    /**
     * The Shortname Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("shortName")
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * The Longname Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("longName")
    public String getLongName() {
        return longName;
    }

    /**
     * The Longname Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("longName")
    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * The Exchangetimezonename Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchangeTimezoneName")
    public String getExchangeTimezoneName() {
        return exchangeTimezoneName;
    }

    /**
     * The Exchangetimezonename Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchangeTimezoneName")
    public void setExchangeTimezoneName(String exchangeTimezoneName) {
        this.exchangeTimezoneName = exchangeTimezoneName;
    }

    @JsonProperty("regularMarketChange")
    public RegularMarketChange getRegularMarketChange() {
        return regularMarketChange;
    }

    @JsonProperty("regularMarketChange")
    public void setRegularMarketChange(RegularMarketChange regularMarketChange) {
        this.regularMarketChange = regularMarketChange;
    }

    @JsonProperty("regularMarketPreviousClose")
    public RegularMarketPreviousClose getRegularMarketPreviousClose() {
        return regularMarketPreviousClose;
    }

    @JsonProperty("regularMarketPreviousClose")
    public void setRegularMarketPreviousClose(RegularMarketPreviousClose regularMarketPreviousClose) {
        this.regularMarketPreviousClose = regularMarketPreviousClose;
    }

    @JsonProperty("fiftyTwoWeekHighChange")
    public FiftyTwoWeekHighChange getFiftyTwoWeekHighChange() {
        return fiftyTwoWeekHighChange;
    }

    @JsonProperty("fiftyTwoWeekHighChange")
    public void setFiftyTwoWeekHighChange(FiftyTwoWeekHighChange fiftyTwoWeekHighChange) {
        this.fiftyTwoWeekHighChange = fiftyTwoWeekHighChange;
    }

    /**
     * The Exchangetimezoneshortname Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchangeTimezoneShortName")
    public String getExchangeTimezoneShortName() {
        return exchangeTimezoneShortName;
    }

    /**
     * The Exchangetimezoneshortname Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchangeTimezoneShortName")
    public void setExchangeTimezoneShortName(String exchangeTimezoneShortName) {
        this.exchangeTimezoneShortName = exchangeTimezoneShortName;
    }

    @JsonProperty("fiftyTwoWeekLowChange")
    public FiftyTwoWeekLowChange getFiftyTwoWeekLowChange() {
        return fiftyTwoWeekLowChange;
    }

    @JsonProperty("fiftyTwoWeekLowChange")
    public void setFiftyTwoWeekLowChange(FiftyTwoWeekLowChange fiftyTwoWeekLowChange) {
        this.fiftyTwoWeekLowChange = fiftyTwoWeekLowChange;
    }

    /**
     * The Exchangedatadelayedby Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchangeDataDelayedBy")
    public Integer getExchangeDataDelayedBy() {
        return exchangeDataDelayedBy;
    }

    /**
     * The Exchangedatadelayedby Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("exchangeDataDelayedBy")
    public void setExchangeDataDelayedBy(Integer exchangeDataDelayedBy) {
        this.exchangeDataDelayedBy = exchangeDataDelayedBy;
    }

    @JsonProperty("regularMarketDayLow")
    public RegularMarketDayLow getRegularMarketDayLow() {
        return regularMarketDayLow;
    }

    @JsonProperty("regularMarketDayLow")
    public void setRegularMarketDayLow(RegularMarketDayLow regularMarketDayLow) {
        this.regularMarketDayLow = regularMarketDayLow;
    }

    /**
     * The Pricehint Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("priceHint")
    public Integer getPriceHint() {
        return priceHint;
    }

    /**
     * The Pricehint Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("priceHint")
    public void setPriceHint(Integer priceHint) {
        this.priceHint = priceHint;
    }

    /**
     * The Currency Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    /**
     * The Currency Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("regularMarketPrice")
    public RegularMarketPrice getRegularMarketPrice() {
        return regularMarketPrice;
    }

    @JsonProperty("regularMarketPrice")
    public void setRegularMarketPrice(RegularMarketPrice regularMarketPrice) {
        this.regularMarketPrice = regularMarketPrice;
    }

    @JsonProperty("regularMarketVolume")
    public RegularMarketVolume getRegularMarketVolume() {
        return regularMarketVolume;
    }

    @JsonProperty("regularMarketVolume")
    public void setRegularMarketVolume(RegularMarketVolume regularMarketVolume) {
        this.regularMarketVolume = regularMarketVolume;
    }

    /**
     * The Isloading Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("isLoading")
    public Boolean getIsLoading() {
        return isLoading;
    }

    /**
     * The Isloading Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("isLoading")
    public void setIsLoading(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    /**
     * The Gmtoffsetmilliseconds Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("gmtOffSetMilliseconds")
    public Integer getGmtOffSetMilliseconds() {
        return gmtOffSetMilliseconds;
    }

    /**
     * The Gmtoffsetmilliseconds Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("gmtOffSetMilliseconds")
    public void setGmtOffSetMilliseconds(Integer gmtOffSetMilliseconds) {
        this.gmtOffSetMilliseconds = gmtOffSetMilliseconds;
    }

    /**
     * The Marketstate Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("marketState")
    public String getMarketState() {
        return marketState;
    }

    /**
     * The Marketstate Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("marketState")
    public void setMarketState(String marketState) {
        this.marketState = marketState;
    }

    @JsonProperty("marketCap")
    public MarketCap getMarketCap() {
        return marketCap;
    }

    @JsonProperty("marketCap")
    public void setMarketCap(MarketCap marketCap) {
        this.marketCap = marketCap;
    }

    /**
     * The Quotetype Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("quoteType")
    public String getQuoteType() {
        return quoteType;
    }

    /**
     * The Quotetype Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("quoteType")
    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    /**
     * The Invalid Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("invalid")
    public Boolean getInvalid() {
        return invalid;
    }

    /**
     * The Invalid Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("invalid")
    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    /**
     * The Symbol Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    /**
     * The Symbol Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * The Language Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    /**
     * The Language Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("fiftyTwoWeekLowChangePercent")
    public FiftyTwoWeekLowChangePercent getFiftyTwoWeekLowChangePercent() {
        return fiftyTwoWeekLowChangePercent;
    }

    @JsonProperty("fiftyTwoWeekLowChangePercent")
    public void setFiftyTwoWeekLowChangePercent(FiftyTwoWeekLowChangePercent fiftyTwoWeekLowChangePercent) {
        this.fiftyTwoWeekLowChangePercent = fiftyTwoWeekLowChangePercent;
    }

    @JsonProperty("regularMarketDayRange")
    public RegularMarketDayRange getRegularMarketDayRange() {
        return regularMarketDayRange;
    }

    @JsonProperty("regularMarketDayRange")
    public void setRegularMarketDayRange(RegularMarketDayRange regularMarketDayRange) {
        this.regularMarketDayRange = regularMarketDayRange;
    }

    /**
     * The Messageboardid Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("messageBoardId")
    public String getMessageBoardId() {
        return messageBoardId;
    }

    /**
     * The Messageboardid Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("messageBoardId")
    public void setMessageBoardId(String messageBoardId) {
        this.messageBoardId = messageBoardId;
    }

    @JsonProperty("fiftyTwoWeekHigh")
    public FiftyTwoWeekHigh getFiftyTwoWeekHigh() {
        return fiftyTwoWeekHigh;
    }

    @JsonProperty("fiftyTwoWeekHigh")
    public void setFiftyTwoWeekHigh(FiftyTwoWeekHigh fiftyTwoWeekHigh) {
        this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
    }

    @JsonProperty("fiftyTwoWeekHighChangePercent")
    public FiftyTwoWeekHighChangePercent getFiftyTwoWeekHighChangePercent() {
        return fiftyTwoWeekHighChangePercent;
    }

    @JsonProperty("fiftyTwoWeekHighChangePercent")
    public void setFiftyTwoWeekHighChangePercent(FiftyTwoWeekHighChangePercent fiftyTwoWeekHighChangePercent) {
        this.fiftyTwoWeekHighChangePercent = fiftyTwoWeekHighChangePercent;
    }

    /**
     * The Uuid Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * The Uuid Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * The Market Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("market")
    public String getMarket() {
        return market;
    }

    /**
     * The Market Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("market")
    public void setMarket(String market) {
        this.market = market;
    }

    @JsonProperty("fiftyTwoWeekLow")
    public FiftyTwoWeekLow getFiftyTwoWeekLow() {
        return fiftyTwoWeekLow;
    }

    @JsonProperty("fiftyTwoWeekLow")
    public void setFiftyTwoWeekLow(FiftyTwoWeekLow fiftyTwoWeekLow) {
        this.fiftyTwoWeekLow = fiftyTwoWeekLow;
    }

    @JsonProperty("regularMarketChangePercent")
    public RegularMarketChangePercent getRegularMarketChangePercent() {
        return regularMarketChangePercent;
    }

    @JsonProperty("regularMarketChangePercent")
    public void setRegularMarketChangePercent(RegularMarketChangePercent regularMarketChangePercent) {
        this.regularMarketChangePercent = regularMarketChangePercent;
    }

    /**
     * The Fullexchangename Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("fullExchangeName")
    public String getFullExchangeName() {
        return fullExchangeName;
    }

    /**
     * The Fullexchangename Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("fullExchangeName")
    public void setFullExchangeName(String fullExchangeName) {
        this.fullExchangeName = fullExchangeName;
    }

    /**
     * The Tradeable Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("tradeable")
    public Boolean getTradeable() {
        return tradeable;
    }

    /**
     * The Tradeable Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("tradeable")
    public void setTradeable(Boolean tradeable) {
        this.tradeable = tradeable;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
