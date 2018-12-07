
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
    "raw",
    "fmt"
})
public class RegularMarketPreviousClose implements Serializable
{

    /**
     * The Raw Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("raw")
    private Integer raw = 0;
    /**
     * The Fmt Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("fmt")
    private String fmt = "";
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -8475820923372120865L;

    /**
     * The Raw Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("raw")
    public Integer getRaw() {
        return raw;
    }

    /**
     * The Raw Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("raw")
    public void setRaw(Integer raw) {
        this.raw = raw;
    }

    /**
     * The Fmt Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("fmt")
    public String getFmt() {
        return fmt;
    }

    /**
     * The Fmt Schema 
     * <p>
     * 
     * 
     */
    @JsonProperty("fmt")
    public void setFmt(String fmt) {
        this.fmt = fmt;
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
