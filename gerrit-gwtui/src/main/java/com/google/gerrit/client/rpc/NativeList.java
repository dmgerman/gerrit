begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractList
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

begin_comment
comment|/** A read-only list of native JavaScript objects stored in a JSON array. */
end_comment

begin_class
DECL|class|NativeList
specifier|public
class|class
name|NativeList
parameter_list|<
name|T
extends|extends
name|JavaScriptObject
parameter_list|>
extends|extends
name|JavaScriptObject
block|{
DECL|method|NativeList ()
specifier|protected
name|NativeList
parameter_list|()
block|{   }
DECL|method|asList ()
specifier|public
specifier|final
name|List
argument_list|<
name|T
argument_list|>
name|asList
parameter_list|()
block|{
return|return
operator|new
name|AbstractList
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|T
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|T
name|element
parameter_list|)
block|{
name|T
name|old
init|=
name|NativeList
operator|.
name|this
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|NativeList
operator|.
name|this
operator|.
name|set0
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
expr_stmt|;
return|return
name|old
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|NativeList
operator|.
name|this
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|NativeList
operator|.
name|this
operator|.
name|size
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|isEmpty ()
specifier|public
specifier|final
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|size
argument_list|()
operator|==
literal|0
return|;
block|}
DECL|method|size ()
specifier|public
specifier|final
specifier|native
name|int
name|size
parameter_list|()
comment|/*-{ return this.length; }-*/
function_decl|;
DECL|method|get (int i)
specifier|public
specifier|final
specifier|native
name|T
name|get
parameter_list|(
name|int
name|i
parameter_list|)
comment|/*-{ return this[i]; }-*/
function_decl|;
DECL|method|set0 (int i, T v)
specifier|private
specifier|final
specifier|native
name|void
name|set0
parameter_list|(
name|int
name|i
parameter_list|,
name|T
name|v
parameter_list|)
comment|/*-{ this[i] = v; }-*/
function_decl|;
block|}
end_class

end_unit

