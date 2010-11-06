/* RefLens, a reference implementation of recommender algorithms.
 * Copyright 2010 Michael Ekstrand <ekstrand@cs.umn.edu>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
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

package org.grouplens.reflens.svd;

import org.grouplens.reflens.RecommendationEngine;
import org.grouplens.reflens.RecommenderBuilder;
import org.grouplens.reflens.data.DataSet;
import org.grouplens.reflens.data.UserRatingProfile;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class FunkSVDFactory<U,I> implements RecommenderBuilder<U,I> {
	
	private Provider<FunkSVD<U,I>> svdProvider;
	@Inject
	public FunkSVDFactory(Provider<FunkSVD<U,I>> provider) {
		svdProvider = provider;
	}

	@Override
	public RecommendationEngine<U, I> build(
			DataSet<UserRatingProfile<U, I>> ratings) {
		FunkSVD<U,I> rec = svdProvider.get();
		rec.build(ratings);
		return rec;
	}

}
