package com.mdmd.util;

import com.alibaba.fastjson.JSONObject;
import com.mdmd.entity.JO.PageJO;
import com.sun.org.apache.regexp.internal.RE;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonObjectForPageAndFilter extends JSONObject {
    private PageJO pageJO;
    private Map<String,Object> filter;
    private Map<String,Object> sort;

    public PageJO getPageJO() throws Exception {
        if(this.pageJO == null)
            if(!this.containsKey("pageNo") || !this.containsKey("pageSize"))
                throw new Exception("无法解析的page对象");
            else
            {
                this.pageJO = new PageJO();
                int pageNo = this.getIntValue("pageNo");
                int pageSize = this.getIntValue("pageSize");
                pageJO.setPageNo(pageNo);
                pageJO.setPageSize(pageSize);
            }
        return this.pageJO;
    }

    public Map<String,Object> getFilter() throws Exception {
        if(this.filter == null)
        {
            this.filter = new HashMap<>();
            if(this.containsKey("filter"))
            {
                JSONObject filter = this.getJSONObject("filter");
                this.filter =  filter.getInnerMap();
            }
        }

        return this.filter;
    }

    public Map<String,Object> getSort() throws Exception {
        if(this.sort == null)
        {
            this.sort = new HashMap<>();
            if(this.containsKey("sort"))
            {
                JSONObject sort = this.getJSONObject("sort");
                if(sort.containsKey("type"))
                {
                    Object type = sort.get("type");
                    if(type.equals("0"))
                    {
                        sort.put("type", "desc");
                    }
                    else
                    {
                        sort.put("type"," ");
                    }
                }
                else
                {
                    this.sort = new HashMap<>();
                    return this.sort;
                }
                this.sort =  sort.getInnerMap();
           }

        }
        return this.sort;
    }

}
