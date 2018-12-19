begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|client
operator|.
name|info
operator|.
name|ChangeInfo
operator|.
name|RevisionInfo
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
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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

begin_comment
comment|/** Cache of PatchSet.Id to revision SHA-1 strings. */
end_comment

begin_class
DECL|class|RevisionInfoCache
specifier|public
class|class
name|RevisionInfoCache
block|{
DECL|field|LIMIT
specifier|private
specifier|static
specifier|final
name|int
name|LIMIT
init|=
literal|10
decl_stmt|;
DECL|field|IMPL
specifier|private
specifier|static
specifier|final
name|RevisionInfoCache
name|IMPL
init|=
operator|new
name|RevisionInfoCache
argument_list|()
decl_stmt|;
DECL|method|add (Change.Id change, RevisionInfo info)
specifier|public
specifier|static
name|void
name|add
parameter_list|(
name|Change
operator|.
name|Id
name|change
parameter_list|,
name|RevisionInfo
name|info
parameter_list|)
block|{
name|IMPL
operator|.
name|psToCommit
operator|.
name|put
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|change
argument_list|,
name|info
operator|.
name|_number
argument_list|()
argument_list|)
argument_list|,
name|info
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|get (PatchSet.Id id)
specifier|static
name|String
name|get
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|IMPL
operator|.
name|psToCommit
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|field|psToCommit
specifier|private
specifier|final
name|LinkedHashMap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|String
argument_list|>
name|psToCommit
decl_stmt|;
DECL|method|RevisionInfoCache ()
specifier|private
name|RevisionInfoCache
parameter_list|()
block|{
name|psToCommit
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|String
argument_list|>
argument_list|(
name|LIMIT
argument_list|)
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|boolean
name|removeEldestEntry
parameter_list|(
name|Map
operator|.
name|Entry
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|String
argument_list|>
name|e
parameter_list|)
block|{
return|return
name|size
argument_list|()
operator|>
name|LIMIT
return|;
block|}
block|}
expr_stmt|;
block|}
block|}
end_class

end_unit

