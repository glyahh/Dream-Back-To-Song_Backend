package com.dbts.dreambacktosong_backend.common;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 通用分页结果封装
 *
 * <p>与前端约定结构保持一致：
 * data: {
 *   list: [],
 *   total: 0,
 *   hasMore: false
 * }
 */
@Data
public class PageResponse<T> {

    /** 列表数据 */
    private List<T> list;

    /** 总条数 */
    private long total;

    /** 是否还有更多数据 */
    private boolean hasMore;

    public PageResponse() {
        this(Collections.emptyList(), 0, false);
    }

    public PageResponse(List<T> list, long total, boolean hasMore) {
        this.list = list;
        this.total = total;
        this.hasMore = hasMore;
    }
}

