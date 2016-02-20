/*
 * LensKit, an open source recommender systems toolkit.
 * Copyright 2010-2014 LensKit Contributors.  See CONTRIBUTORS.md.
 * Work on LensKit has been funded by the National Science Foundation under
 * grants IIS 05-34939, 08-08692, 08-12148, and 10-17697.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.lenskit.data.entities;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Entities {
    private Entities() {}

    /**
     * Create a new bare entity.
     * @param type The bare entity.
     * @param id The entity ID.
     * @return An entity.
     */
    public static Entity create(EntityType type, long id) {
        return new BareEntity(type, id);
    }

    /**
     * Create a new basic entity builder.
     * @param type The entity type.
     * @return The entity builder.
     */
    public static EntityBuilder newBuilder(EntityType type) {
        return new BasicEntityBuilder(type);
    }

    /**
     * Create a new basic entity builder.
     * @param id The entity ID.
     * @return The entity builder.
     */
    public static EntityBuilder newBuilder(EntityType type, long id) {
        return newBuilder(type).setId(id);
    }

    /**
     * Create a new entity builder that is initialized with a copy of an entity.
     * @param e The entity.
     * @return An entity builder initialized to build a copy of {@code e}.
     */
    public static EntityBuilder copyBuilder(Entity e) {
        EntityBuilder eb = newBuilder(e.getType(), e.getId());
        for (TypedName a: e.getTypedAttributeNames()) {
            eb.setAttribute(a, e.get(a));
        }
        return eb;
    }

    /**
     * Create a predicate that filters events for an entity type.
     * @param type The entity type.
     * @return A predicate matching entities of type `type`.
     */
    public static Predicate<Entity> typePredicate(final EntityType type) {
        return new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity input) {
                return (input != null) && input.getType().equals(type);
            }
        };
    }

    /**
     * Create a predicate that filters events for an ID.
     * @param id The ID sought.
     * @return A predicate matching entities with id `id`.
     */
    public static Predicate<Entity> idPredicate(final long id) {
        return new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity input) {
                return (input != null) && input.getId() == id;
            }
        };
    }

    public static <T> Function<Entity,T> attributeValueFunction(final Attribute<T> attr) {
        return new Function<Entity, T>() {
            @Nullable
            @Override
            public T apply(@Nullable Entity input) {
                return input == null ? null : input.maybeGet(attr);
            }
        };
    }

    /**
     * Project an entity to a target view type.
     * @param e The entity to project.
     * @param viewClass The view type.
     * @param <E> The view type.
     * @return The projected entity.
     */
    public static <E extends Entity> E project(@Nonnull Entity e, @Nonnull Class<E> viewClass) {
        if (viewClass.isInstance(e)) {
            return viewClass.cast(e);
        } else {
            throw new IllegalArgumentException("entity type " + e.getClass() + " cannot be projected to " + viewClass);
        }
    }

    /**
     * Create a projection function that maps entities to a new view.
     * @param viewClass The target view class type.
     * @param <E> The entity type.
     * @return A function that will project entities.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Entity> Function<Entity,E> projection(final Class<E> viewClass) {
        if (viewClass.equals(Entity.class)) {
            return (Function) Functions.identity();
        } else {
            return new Function<Entity, E>() {
                @Nullable
                @Override
                public E apply(@Nullable Entity input) {
                    assert input != null;
                    return project(input, viewClass);
                }
            };
        }
    }
}