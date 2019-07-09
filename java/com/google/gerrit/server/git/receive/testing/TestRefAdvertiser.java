begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git.receive.testing
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
operator|.
name|receive
operator|.
name|testing
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
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|annotations
operator|.
name|VisibleForTesting
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
name|HashMap
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|StreamSupport
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|RefAdvertiser
import|;
end_import

begin_comment
comment|/** Helper to collect advertised refs and additonal haves and verify them in tests. */
end_comment

begin_class
DECL|class|TestRefAdvertiser
specifier|public
class|class
name|TestRefAdvertiser
extends|extends
name|RefAdvertiser
block|{
annotation|@
name|VisibleForTesting
annotation|@
name|AutoValue
DECL|class|Result
specifier|public
specifier|abstract
specifier|static
class|class
name|Result
block|{
DECL|method|allRefs ()
specifier|public
specifier|abstract
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|allRefs
parameter_list|()
function_decl|;
DECL|method|additionalHaves ()
specifier|public
specifier|abstract
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|additionalHaves
parameter_list|()
function_decl|;
DECL|method|create (Map<String, Ref> allRefs, Set<ObjectId> additionalHaves)
specifier|public
specifier|static
name|Result
name|create
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|allRefs
parameter_list|,
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|additionalHaves
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_TestRefAdvertiser_Result
argument_list|(
name|allRefs
argument_list|,
name|additionalHaves
argument_list|)
return|;
block|}
block|}
DECL|field|advertisedRefs
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|advertisedRefs
decl_stmt|;
DECL|field|additionalHaves
specifier|private
specifier|final
name|Set
argument_list|<
name|ObjectId
argument_list|>
name|additionalHaves
decl_stmt|;
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|method|TestRefAdvertiser (Repository repo)
specifier|public
name|TestRefAdvertiser
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
name|advertisedRefs
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|additionalHaves
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|writeOne (CharSequence line)
specifier|protected
name|void
name|writeOne
parameter_list|(
name|CharSequence
name|line
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|lineParts
init|=
name|StreamSupport
operator|.
name|stream
argument_list|(
name|Splitter
operator|.
name|on
argument_list|(
literal|' '
argument_list|)
operator|.
name|split
argument_list|(
name|line
argument_list|)
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|map
argument_list|(
name|String
operator|::
name|trim
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|".have"
operator|.
name|equals
argument_list|(
name|lineParts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
condition|)
block|{
name|additionalHaves
operator|.
name|add
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|lineParts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|lineParts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|Ref
name|ref
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|r
lambda|->
name|r
operator|.
name|getObjectId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|RuntimeException
argument_list|(
name|line
operator|.
name|toString
argument_list|()
operator|+
literal|" does not conform to expected pattern"
argument_list|)
argument_list|)
decl_stmt|;
name|advertisedRefs
operator|.
name|put
argument_list|(
name|lineParts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|end ()
specifier|protected
name|void
name|end
parameter_list|()
block|{}
DECL|method|result ()
specifier|public
name|Result
name|result
parameter_list|()
block|{
return|return
name|Result
operator|.
name|create
argument_list|(
name|advertisedRefs
argument_list|,
name|additionalHaves
argument_list|)
return|;
block|}
block|}
end_class

end_unit
