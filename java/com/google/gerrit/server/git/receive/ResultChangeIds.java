begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|Change
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
name|EnumMap
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

begin_comment
comment|/**  * Keeps track of the change IDs thus far updated by ReceiveCommit.  *  *<p>This class is thread-safe.  */
end_comment

begin_class
DECL|class|ResultChangeIds
specifier|public
class|class
name|ResultChangeIds
block|{
DECL|enum|Key
specifier|public
enum|enum
name|Key
block|{
DECL|enumConstant|CREATED
name|CREATED
block|,
DECL|enumConstant|REPLACED
name|REPLACED
block|,
DECL|enumConstant|AUTOCLOSED
name|AUTOCLOSED
block|,   }
DECL|field|isMagicPush
specifier|private
name|boolean
name|isMagicPush
decl_stmt|;
DECL|field|ids
specifier|private
specifier|final
name|Map
argument_list|<
name|Key
argument_list|,
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|>
name|ids
decl_stmt|;
DECL|method|ResultChangeIds ()
name|ResultChangeIds
parameter_list|()
block|{
name|ids
operator|=
operator|new
name|EnumMap
argument_list|<>
argument_list|(
name|Key
operator|.
name|class
argument_list|)
expr_stmt|;
for|for
control|(
name|Key
name|k
range|:
name|Key
operator|.
name|values
argument_list|()
control|)
block|{
name|ids
operator|.
name|put
argument_list|(
name|k
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Record a change ID update as having completed. Thread-safe. */
DECL|method|add (Key key, Change.Id id)
specifier|public
specifier|synchronized
name|void
name|add
parameter_list|(
name|Key
name|key
parameter_list|,
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
name|ids
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
comment|/** Indicate that the ReceiveCommits call involved a magic branch. */
DECL|method|setMagicPush (boolean magic)
specifier|public
specifier|synchronized
name|void
name|setMagicPush
parameter_list|(
name|boolean
name|magic
parameter_list|)
block|{
name|isMagicPush
operator|=
name|magic
expr_stmt|;
block|}
DECL|method|isMagicPush ()
specifier|public
specifier|synchronized
name|boolean
name|isMagicPush
parameter_list|()
block|{
return|return
name|isMagicPush
return|;
block|}
comment|/**    * Returns change IDs of the given type for which the BatchUpdate succeeded, or empty list if    * there are none. Thread-safe.    */
DECL|method|get (Key key)
specifier|public
specifier|synchronized
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|get
parameter_list|(
name|Key
name|key
parameter_list|)
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|ids
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

