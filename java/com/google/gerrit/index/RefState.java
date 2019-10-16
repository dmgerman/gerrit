begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
operator|.
name|firstNonNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Splitter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|MultimapBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|SetMultimap
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
name|common
operator|.
name|Nullable
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
name|entities
operator|.
name|Project
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
name|git
operator|.
name|ObjectIds
import|;
end_import

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
annotation|@
name|AutoValue
DECL|class|RefState
specifier|public
specifier|abstract
class|class
name|RefState
block|{
DECL|method|parseStates (Iterable<byte[]> states)
specifier|public
specifier|static
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefState
argument_list|>
name|parseStates
parameter_list|(
name|Iterable
argument_list|<
name|byte
index|[]
argument_list|>
name|states
parameter_list|)
block|{
name|RefState
operator|.
name|check
argument_list|(
name|states
operator|!=
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|RefState
argument_list|>
name|result
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|hashSetValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
index|[]
name|b
range|:
name|states
control|)
block|{
name|RefState
operator|.
name|check
argument_list|(
name|b
operator|!=
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|b
argument_list|,
name|UTF_8
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|':'
argument_list|)
operator|.
name|splitToList
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|RefState
operator|.
name|check
argument_list|(
name|parts
operator|.
name|size
argument_list|()
operator|==
literal|3
operator|&&
operator|!
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|,
name|RefState
operator|.
name|create
argument_list|(
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|parts
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|create (String ref, String sha)
specifier|public
specifier|static
name|RefState
name|create
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|sha
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_RefState
argument_list|(
name|ref
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
name|sha
argument_list|)
argument_list|)
return|;
block|}
DECL|method|create (String ref, @Nullable ObjectId id)
specifier|public
specifier|static
name|RefState
name|create
parameter_list|(
name|String
name|ref
parameter_list|,
annotation|@
name|Nullable
name|ObjectId
name|id
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_RefState
argument_list|(
name|ref
argument_list|,
name|firstNonNull
argument_list|(
name|id
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|of (Ref ref)
specifier|public
specifier|static
name|RefState
name|of
parameter_list|(
name|Ref
name|ref
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_RefState
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|toByteArray (Project.NameKey project)
specifier|public
name|byte
index|[]
name|toByteArray
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|byte
index|[]
name|a
init|=
operator|(
name|project
operator|.
name|toString
argument_list|()
operator|+
literal|':'
operator|+
name|ref
argument_list|()
operator|+
literal|':'
operator|)
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
decl_stmt|;
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
name|a
operator|.
name|length
operator|+
name|ObjectIds
operator|.
name|STR_LEN
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|a
argument_list|,
literal|0
argument_list|,
name|b
argument_list|,
literal|0
argument_list|,
name|a
operator|.
name|length
argument_list|)
expr_stmt|;
name|id
argument_list|()
operator|.
name|copyTo
argument_list|(
name|b
argument_list|,
name|a
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
DECL|method|check (boolean condition, String str)
specifier|public
specifier|static
name|void
name|check
parameter_list|(
name|boolean
name|condition
parameter_list|,
name|String
name|str
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|condition
argument_list|,
literal|"invalid RefState: %s"
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
DECL|method|ref ()
specifier|public
specifier|abstract
name|String
name|ref
parameter_list|()
function_decl|;
DECL|method|id ()
specifier|public
specifier|abstract
name|ObjectId
name|id
parameter_list|()
function_decl|;
DECL|method|match (Repository repo)
specifier|public
name|boolean
name|match
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
name|Ref
name|ref
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|ref
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectId
name|expected
init|=
name|ref
operator|!=
literal|null
condition|?
name|ref
operator|.
name|getObjectId
argument_list|()
else|:
name|ObjectId
operator|.
name|zeroId
argument_list|()
decl_stmt|;
return|return
name|id
argument_list|()
operator|.
name|equals
argument_list|(
name|expected
argument_list|)
return|;
block|}
block|}
end_class

end_unit

