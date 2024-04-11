package com.highwayac.com.repository;

import com.highwayac.com.domain.Member;
import com.highwayac.com.repository.rowmapper.MemberRowMapper;
import com.highwayac.com.repository.rowmapper.WorkspaceRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Member entity.
 */
@SuppressWarnings("unused")
class MemberRepositoryInternalImpl extends SimpleR2dbcRepository<Member, Long> implements MemberRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkspaceRowMapper workspaceMapper;
    private final MemberRowMapper memberMapper;

    private static final Table entityTable = Table.aliased("member", EntityManager.ENTITY_ALIAS);
    private static final Table workspaceTable = Table.aliased("workspace", "workspace");

    public MemberRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WorkspaceRowMapper workspaceMapper,
        MemberRowMapper memberMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Member.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workspaceMapper = workspaceMapper;
        this.memberMapper = memberMapper;
    }

    @Override
    public Flux<Member> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Member> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MemberSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkspaceSqlHelper.getColumns(workspaceTable, "workspace"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(workspaceTable)
            .on(Column.create("workspace_id", entityTable))
            .equals(Column.create("id", workspaceTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Member.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Member> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Member> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Member process(Row row, RowMetadata metadata) {
        Member entity = memberMapper.apply(row, "e");
        entity.setWorkspace(workspaceMapper.apply(row, "workspace"));
        return entity;
    }

    @Override
    public <S extends Member> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
