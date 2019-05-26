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
DECL|package|com.google.gerrit.server.git.receive
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
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|RefNames
operator|.
name|REFS_CACHE_AUTOMERGE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|RefNames
operator|.
name|REFS_CHANGES
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
name|Maps
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
name|transport
operator|.
name|RefFilter
import|;
end_import

begin_class
DECL|class|ReceiveRefFilter
class|class
name|ReceiveRefFilter
implements|implements
name|RefFilter
block|{
annotation|@
name|Override
DECL|method|filter (Map<String, Ref> refs)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|filter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refs
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|filteredRefs
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|refs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|e
range|:
name|refs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|startsWith
argument_list|(
name|REFS_CHANGES
argument_list|)
operator|&&
operator|!
name|name
operator|.
name|startsWith
argument_list|(
name|REFS_CACHE_AUTOMERGE
argument_list|)
condition|)
block|{
name|filteredRefs
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|filteredRefs
return|;
block|}
block|}
end_class

end_unit

