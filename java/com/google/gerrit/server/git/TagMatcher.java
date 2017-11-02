begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|server
operator|.
name|git
operator|.
name|TagSet
operator|.
name|Tag
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|BitSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Ref
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_class
DECL|class|TagMatcher
class|class
name|TagMatcher
block|{
DECL|field|mask
specifier|final
name|BitSet
name|mask
init|=
operator|new
name|BitSet
argument_list|()
decl_stmt|;
DECL|field|newRefs
specifier|final
name|List
argument_list|<
name|Ref
argument_list|>
name|newRefs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|lostRefs
specifier|final
name|List
argument_list|<
name|LostRef
argument_list|>
name|lostRefs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|holder
specifier|final
name|TagSetHolder
name|holder
decl_stmt|;
DECL|field|cache
specifier|final
name|TagCache
name|cache
decl_stmt|;
DECL|field|db
specifier|final
name|Repository
name|db
decl_stmt|;
DECL|field|include
specifier|final
name|Collection
argument_list|<
name|Ref
argument_list|>
name|include
decl_stmt|;
DECL|field|tags
name|TagSet
name|tags
decl_stmt|;
DECL|field|updated
specifier|final
name|boolean
name|updated
decl_stmt|;
DECL|field|rebuiltForNewTags
specifier|private
name|boolean
name|rebuiltForNewTags
decl_stmt|;
DECL|method|TagMatcher ( TagSetHolder holder, TagCache cache, Repository db, Collection<Ref> include, TagSet tags, boolean updated)
name|TagMatcher
parameter_list|(
name|TagSetHolder
name|holder
parameter_list|,
name|TagCache
name|cache
parameter_list|,
name|Repository
name|db
parameter_list|,
name|Collection
argument_list|<
name|Ref
argument_list|>
name|include
parameter_list|,
name|TagSet
name|tags
parameter_list|,
name|boolean
name|updated
parameter_list|)
block|{
name|this
operator|.
name|holder
operator|=
name|holder
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|include
operator|=
name|include
expr_stmt|;
name|this
operator|.
name|tags
operator|=
name|tags
expr_stmt|;
name|this
operator|.
name|updated
operator|=
name|updated
expr_stmt|;
block|}
DECL|method|isReachable (Ref tagRef)
name|boolean
name|isReachable
parameter_list|(
name|Ref
name|tagRef
parameter_list|)
block|{
name|tagRef
operator|=
name|db
operator|.
name|peel
argument_list|(
name|tagRef
argument_list|)
expr_stmt|;
name|ObjectId
name|tagObj
init|=
name|tagRef
operator|.
name|getPeeledObjectId
argument_list|()
decl_stmt|;
if|if
condition|(
name|tagObj
operator|==
literal|null
condition|)
block|{
name|tagObj
operator|=
name|tagRef
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
if|if
condition|(
name|tagObj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|Tag
name|tag
init|=
name|tags
operator|.
name|lookupTag
argument_list|(
name|tagObj
argument_list|)
decl_stmt|;
if|if
condition|(
name|tag
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|rebuiltForNewTags
condition|)
block|{
return|return
literal|false
return|;
block|}
name|rebuiltForNewTags
operator|=
literal|true
expr_stmt|;
name|holder
operator|.
name|rebuildForNewTags
argument_list|(
name|cache
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|isReachable
argument_list|(
name|tagRef
argument_list|)
return|;
block|}
return|return
name|tag
operator|.
name|has
argument_list|(
name|mask
argument_list|)
return|;
block|}
DECL|class|LostRef
specifier|static
class|class
name|LostRef
block|{
DECL|field|tag
specifier|final
name|Tag
name|tag
decl_stmt|;
DECL|field|flag
specifier|final
name|int
name|flag
decl_stmt|;
DECL|method|LostRef (Tag tag, int flag)
name|LostRef
parameter_list|(
name|Tag
name|tag
parameter_list|,
name|int
name|flag
parameter_list|)
block|{
name|this
operator|.
name|tag
operator|=
name|tag
expr_stmt|;
name|this
operator|.
name|flag
operator|=
name|flag
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
