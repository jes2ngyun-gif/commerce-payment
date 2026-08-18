package com.jes2ngyun.commerce_payment.product.repository;

import com.jes2ngyun.commerce_payment.product.dto.request.ProductSearchCondition;
import com.jes2ngyun.commerce_payment.product.entity.Category;
import com.jes2ngyun.commerce_payment.product.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.jes2ngyun.commerce_payment.product.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> search(ProductSearchCondition condition, Pageable pageable) {

        List<Product> content = queryFactory
                .selectFrom(product)
                .where(
                        categoryEq(condition.category()),
                        priceGoe(condition.minPrice()),
                        priceLoe(condition.maxPrice())
                )
                .orderBy(product.createdAt.desc(), product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        categoryEq(condition.category()),
                        priceGoe(condition.minPrice()),
                        priceLoe(condition.maxPrice())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? product.category.eq(category) : null;
    }

    private BooleanExpression priceGoe(Integer minPrice) {
        return minPrice != null ? product.price.goe(minPrice) : null;
    }

    private BooleanExpression priceLoe(Integer maxPrice) {
        return maxPrice != null ? product.price.loe(maxPrice) : null;
    }
}