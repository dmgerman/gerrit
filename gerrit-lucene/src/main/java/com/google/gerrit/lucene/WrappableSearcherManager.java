begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|com.google.gerrit.lucene
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|lucene
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|DirectoryReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|FilterDirectoryReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|FilterLeafReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|IndexSearcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|ReferenceManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|SearcherFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|store
operator|.
name|Directory
import|;
end_import

begin_comment
comment|/**  * Utility class to safely share {@link IndexSearcher} instances across multiple threads, while  * periodically reopening. This class ensures each searcher is closed only once all threads have  * finished using it.  *  *<p>Use {@link #acquire} to obtain the current searcher, and {@link #release} to release it, like  * this:  *  *<pre class="prettyprint">  * IndexSearcher s = manager.acquire();  * try {  *   // Do searching, doc retrieval, etc. with s  * } finally {  *   manager.release(s);  * }  * // Do not use s after this!  * s = null;  *</pre>  *  *<p>In addition you should periodically call {@link #maybeRefresh}. While it's possible to call  * this just before running each query, this is discouraged since it penalizes the unlucky queries  * that need to refresh. It's better to use a separate background thread, that periodically calls  * {@link #maybeRefresh}. Finally, be sure to call {@link #close} once you are done.  *  * @see SearcherFactory  * @lucene.experimental  */
end_comment

begin_comment
comment|// This file was copied from:
end_comment

begin_comment
comment|// https://github.com/apache/lucene-solr/blob/lucene_solr_5_0/lucene/core/src/java/org/apache/lucene/search/SearcherManager.java
end_comment

begin_comment
comment|// The only change (other than class name and import fixes)
end_comment

begin_comment
comment|// is to skip the check in getSearcher that searcherFactory.newSearcher wraps
end_comment

begin_comment
comment|// the provided searcher exactly.
end_comment

begin_class
DECL|class|WrappableSearcherManager
specifier|final
class|class
name|WrappableSearcherManager
extends|extends
name|ReferenceManager
argument_list|<
name|IndexSearcher
argument_list|>
block|{
DECL|field|searcherFactory
specifier|private
specifier|final
name|SearcherFactory
name|searcherFactory
decl_stmt|;
comment|/**    * Creates and returns a new SearcherManager from the given {@link IndexWriter}.    *    * @param writer the IndexWriter to open the IndexReader from.    * @param applyAllDeletes If<code>true</code>, all buffered deletes will be applied (made    *     visible) in the {@link IndexSearcher} / {@link DirectoryReader}. If<code>false</code>, the    *     deletes may or may not be applied, but remain buffered (in IndexWriter) so that they will    *     be applied in the future. Applying deletes can be costly, so if your app can tolerate    *     deleted documents being returned you might gain some performance by passing<code>false    *</code>. See {@link DirectoryReader#openIfChanged(DirectoryReader, IndexWriter, boolean)}.    * @param searcherFactory An optional {@link SearcherFactory}. Pass<code>null</code> if you don't    *     require the searcher to be warmed before going live or other custom behavior.    * @throws IOException if there is a low-level I/O error    */
DECL|method|WrappableSearcherManager ( IndexWriter writer, boolean applyAllDeletes, SearcherFactory searcherFactory)
name|WrappableSearcherManager
parameter_list|(
name|IndexWriter
name|writer
parameter_list|,
name|boolean
name|applyAllDeletes
parameter_list|,
name|SearcherFactory
name|searcherFactory
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|searcherFactory
operator|==
literal|null
condition|)
block|{
name|searcherFactory
operator|=
operator|new
name|SearcherFactory
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|searcherFactory
operator|=
name|searcherFactory
expr_stmt|;
name|current
operator|=
name|getSearcher
argument_list|(
name|searcherFactory
argument_list|,
name|DirectoryReader
operator|.
name|open
argument_list|(
name|writer
argument_list|,
name|applyAllDeletes
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates and returns a new SearcherManager from the given {@link Directory}.    *    * @param dir the directory to open the DirectoryReader on.    * @param searcherFactory An optional {@link SearcherFactory}. Pass<code>null</code> if you don't    *     require the searcher to be warmed before going live or other custom behavior.    * @throws IOException if there is a low-level I/O error    */
DECL|method|WrappableSearcherManager (Directory dir, SearcherFactory searcherFactory)
name|WrappableSearcherManager
parameter_list|(
name|Directory
name|dir
parameter_list|,
name|SearcherFactory
name|searcherFactory
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|searcherFactory
operator|==
literal|null
condition|)
block|{
name|searcherFactory
operator|=
operator|new
name|SearcherFactory
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|searcherFactory
operator|=
name|searcherFactory
expr_stmt|;
name|current
operator|=
name|getSearcher
argument_list|(
name|searcherFactory
argument_list|,
name|DirectoryReader
operator|.
name|open
argument_list|(
name|dir
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates and returns a new SearcherManager from an existing {@link DirectoryReader}. Note that    * this steals the incoming reference.    *    * @param reader the DirectoryReader.    * @param searcherFactory An optional {@link SearcherFactory}. Pass<code>null</code> if you don't    *     require the searcher to be warmed before going live or other custom behavior.    * @throws IOException if there is a low-level I/O error    */
DECL|method|WrappableSearcherManager (DirectoryReader reader, SearcherFactory searcherFactory)
name|WrappableSearcherManager
parameter_list|(
name|DirectoryReader
name|reader
parameter_list|,
name|SearcherFactory
name|searcherFactory
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|searcherFactory
operator|==
literal|null
condition|)
block|{
name|searcherFactory
operator|=
operator|new
name|SearcherFactory
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|searcherFactory
operator|=
name|searcherFactory
expr_stmt|;
name|this
operator|.
name|current
operator|=
name|getSearcher
argument_list|(
name|searcherFactory
argument_list|,
name|reader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|decRef (IndexSearcher reference)
specifier|protected
name|void
name|decRef
parameter_list|(
name|IndexSearcher
name|reference
parameter_list|)
throws|throws
name|IOException
block|{
name|reference
operator|.
name|getIndexReader
argument_list|()
operator|.
name|decRef
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|refreshIfNeeded (IndexSearcher referenceToRefresh)
specifier|protected
name|IndexSearcher
name|refreshIfNeeded
parameter_list|(
name|IndexSearcher
name|referenceToRefresh
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|IndexReader
name|r
init|=
name|referenceToRefresh
operator|.
name|getIndexReader
argument_list|()
decl_stmt|;
assert|assert
name|r
operator|instanceof
name|DirectoryReader
operator|:
literal|"searcher's IndexReader should be a DirectoryReader, but got "
operator|+
name|r
assert|;
specifier|final
name|IndexReader
name|newReader
init|=
name|DirectoryReader
operator|.
name|openIfChanged
argument_list|(
operator|(
name|DirectoryReader
operator|)
name|r
argument_list|)
decl_stmt|;
if|if
condition|(
name|newReader
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getSearcher
argument_list|(
name|searcherFactory
argument_list|,
name|newReader
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tryIncRef (IndexSearcher reference)
specifier|protected
name|boolean
name|tryIncRef
parameter_list|(
name|IndexSearcher
name|reference
parameter_list|)
block|{
return|return
name|reference
operator|.
name|getIndexReader
argument_list|()
operator|.
name|tryIncRef
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRefCount (IndexSearcher reference)
specifier|protected
name|int
name|getRefCount
parameter_list|(
name|IndexSearcher
name|reference
parameter_list|)
block|{
return|return
name|reference
operator|.
name|getIndexReader
argument_list|()
operator|.
name|getRefCount
argument_list|()
return|;
block|}
comment|/**    * Returns<code>true</code> if no changes have occured since this searcher ie. reader was opened,    * otherwise<code>false</code>.    *    * @see DirectoryReader#isCurrent()    */
DECL|method|isSearcherCurrent ()
specifier|public
name|boolean
name|isSearcherCurrent
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|IndexSearcher
name|searcher
init|=
name|acquire
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|IndexReader
name|r
init|=
name|searcher
operator|.
name|getIndexReader
argument_list|()
decl_stmt|;
assert|assert
name|r
operator|instanceof
name|DirectoryReader
operator|:
literal|"searcher's IndexReader should be a DirectoryReader, but got "
operator|+
name|r
assert|;
return|return
operator|(
operator|(
name|DirectoryReader
operator|)
name|r
operator|)
operator|.
name|isCurrent
argument_list|()
return|;
block|}
finally|finally
block|{
name|release
argument_list|(
name|searcher
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Expert: creates a searcher from the provided {@link IndexReader} using the provided {@link    * SearcherFactory}. NOTE: this decRefs incoming reader on throwing an exception.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"resource"
argument_list|)
DECL|method|getSearcher (SearcherFactory searcherFactory, IndexReader reader)
specifier|public
specifier|static
name|IndexSearcher
name|getSearcher
parameter_list|(
name|SearcherFactory
name|searcherFactory
parameter_list|,
name|IndexReader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|success
init|=
literal|false
decl_stmt|;
specifier|final
name|IndexSearcher
name|searcher
decl_stmt|;
try|try
block|{
name|searcher
operator|=
name|searcherFactory
operator|.
name|newSearcher
argument_list|(
name|reader
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Modification for Gerrit: Allow searcherFactory to transitively wrap the
comment|// provided reader.
name|IndexReader
name|unwrapped
init|=
name|searcher
operator|.
name|getIndexReader
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|unwrapped
operator|==
name|reader
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|unwrapped
operator|instanceof
name|FilterDirectoryReader
condition|)
block|{
name|unwrapped
operator|=
operator|(
operator|(
name|FilterDirectoryReader
operator|)
name|unwrapped
operator|)
operator|.
name|getDelegate
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|unwrapped
operator|instanceof
name|FilterLeafReader
condition|)
block|{
name|unwrapped
operator|=
operator|(
operator|(
name|FilterLeafReader
operator|)
name|unwrapped
operator|)
operator|.
name|getDelegate
argument_list|()
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
if|if
condition|(
name|unwrapped
operator|!=
name|reader
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"SearcherFactory must wrap the provided reader (got "
operator|+
name|searcher
operator|.
name|getIndexReader
argument_list|()
operator|+
literal|" but expected "
operator|+
name|reader
operator|+
literal|")"
argument_list|)
throw|;
block|}
name|success
operator|=
literal|true
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|success
condition|)
block|{
name|reader
operator|.
name|decRef
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|searcher
return|;
block|}
block|}
end_class

end_unit

