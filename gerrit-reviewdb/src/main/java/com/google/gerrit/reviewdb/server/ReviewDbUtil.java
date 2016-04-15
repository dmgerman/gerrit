begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.reviewdb.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
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
name|base
operator|.
name|Function
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
name|Ordering
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
name|gwtorm
operator|.
name|client
operator|.
name|IntKey
import|;
end_import

begin_comment
comment|/** Static utilities for ReviewDb types. */
end_comment

begin_class
DECL|class|ReviewDbUtil
specifier|public
class|class
name|ReviewDbUtil
block|{
DECL|field|INT_KEY_FUNCTION
specifier|private
specifier|static
specifier|final
name|Function
argument_list|<
name|IntKey
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|INT_KEY_FUNCTION
init|=
operator|new
name|Function
argument_list|<
name|IntKey
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|apply
parameter_list|(
name|IntKey
argument_list|<
name|?
argument_list|>
name|in
parameter_list|)
block|{
return|return
name|in
operator|.
name|get
argument_list|()
return|;
block|}
block|}
empty_stmt|;
DECL|field|CHANGE_ID_FUNCTION
specifier|private
specifier|static
specifier|final
name|Function
argument_list|<
name|Change
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|CHANGE_ID_FUNCTION
init|=
operator|new
name|Function
argument_list|<
name|Change
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Change
operator|.
name|Id
name|apply
parameter_list|(
name|Change
name|in
parameter_list|)
block|{
return|return
name|in
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|INT_KEY_ORDERING
specifier|private
specifier|static
specifier|final
name|Ordering
argument_list|<
name|?
extends|extends
name|IntKey
argument_list|<
name|?
argument_list|>
argument_list|>
name|INT_KEY_ORDERING
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
operator|.
name|onResultOf
argument_list|(
name|INT_KEY_FUNCTION
argument_list|)
operator|.
name|nullsFirst
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|intKeyOrdering ()
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|IntKey
argument_list|<
name|?
argument_list|>
parameter_list|>
name|Ordering
argument_list|<
name|K
argument_list|>
name|intKeyOrdering
parameter_list|()
block|{
return|return
operator|(
name|Ordering
argument_list|<
name|K
argument_list|>
operator|)
name|INT_KEY_ORDERING
return|;
block|}
DECL|method|changeIdFunction ()
specifier|public
specifier|static
name|Function
argument_list|<
name|Change
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|changeIdFunction
parameter_list|()
block|{
return|return
name|CHANGE_ID_FUNCTION
return|;
block|}
DECL|method|ReviewDbUtil ()
specifier|private
name|ReviewDbUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

