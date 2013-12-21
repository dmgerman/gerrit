begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_class
DECL|class|ChangeDataResultSet
specifier|abstract
class|class
name|ChangeDataResultSet
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractResultSet
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|method|change (final ChangeData.Factory factory, final Provider<ReviewDb> db, final ResultSet<Change> rs)
specifier|static
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
name|change
parameter_list|(
specifier|final
name|ChangeData
operator|.
name|Factory
name|factory
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
specifier|final
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|rs
parameter_list|)
block|{
return|return
operator|new
name|ChangeDataResultSet
argument_list|<
name|Change
argument_list|>
argument_list|(
name|rs
argument_list|,
literal|true
argument_list|)
block|{
annotation|@
name|Override
name|ChangeData
name|convert
parameter_list|(
name|Change
name|t
parameter_list|)
block|{
return|return
name|factory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|t
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|patchSet (final ChangeData.Factory factory, final Provider<ReviewDb> db, final ResultSet<PatchSet> rs)
specifier|static
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
name|patchSet
parameter_list|(
specifier|final
name|ChangeData
operator|.
name|Factory
name|factory
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
specifier|final
name|ResultSet
argument_list|<
name|PatchSet
argument_list|>
name|rs
parameter_list|)
block|{
return|return
operator|new
name|ChangeDataResultSet
argument_list|<
name|PatchSet
argument_list|>
argument_list|(
name|rs
argument_list|,
literal|false
argument_list|)
block|{
annotation|@
name|Override
name|ChangeData
name|convert
parameter_list|(
name|PatchSet
name|t
parameter_list|)
block|{
return|return
name|factory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|t
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|field|source
specifier|private
specifier|final
name|ResultSet
argument_list|<
name|T
argument_list|>
name|source
decl_stmt|;
DECL|field|unique
specifier|private
specifier|final
name|boolean
name|unique
decl_stmt|;
DECL|method|ChangeDataResultSet (ResultSet<T> source, boolean unique)
name|ChangeDataResultSet
parameter_list|(
name|ResultSet
argument_list|<
name|T
argument_list|>
name|source
parameter_list|,
name|boolean
name|unique
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
name|this
operator|.
name|unique
operator|=
name|unique
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|ChangeData
argument_list|>
name|iterator
parameter_list|()
block|{
if|if
condition|(
name|unique
condition|)
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|ChangeData
argument_list|>
argument_list|()
block|{
specifier|private
specifier|final
name|Iterator
argument_list|<
name|T
argument_list|>
name|itr
init|=
name|source
operator|.
name|iterator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|itr
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ChangeData
name|next
parameter_list|()
block|{
return|return
name|convert
argument_list|(
name|itr
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
else|else
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|ChangeData
argument_list|>
argument_list|()
block|{
specifier|private
specifier|final
name|Iterator
argument_list|<
name|T
argument_list|>
name|itr
init|=
name|source
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ChangeData
name|next
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
if|if
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ChangeData
name|d
init|=
name|convert
argument_list|(
name|itr
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|seen
operator|.
name|add
argument_list|(
name|d
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|next
operator|=
name|d
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|ChangeData
name|next
parameter_list|()
block|{
if|if
condition|(
name|hasNext
argument_list|()
condition|)
block|{
name|ChangeData
name|r
init|=
name|next
decl_stmt|;
name|next
operator|=
literal|null
expr_stmt|;
return|return
name|r
return|;
block|}
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|source
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|convert (T t)
specifier|abstract
name|ChangeData
name|convert
parameter_list|(
name|T
name|t
parameter_list|)
function_decl|;
block|}
end_class

end_unit

