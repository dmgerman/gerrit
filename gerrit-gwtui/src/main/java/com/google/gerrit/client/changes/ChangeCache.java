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
name|reviewdb
operator|.
name|client
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
name|HashMap
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
comment|/** A Cache to store common client side data by change */
end_comment

begin_class
DECL|class|ChangeCache
specifier|public
class|class
name|ChangeCache
block|{
DECL|field|caches
specifier|private
specifier|static
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeCache
argument_list|>
name|caches
init|=
operator|new
name|HashMap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeCache
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|get (Change.Id chg)
specifier|public
specifier|static
name|ChangeCache
name|get
parameter_list|(
name|Change
operator|.
name|Id
name|chg
parameter_list|)
block|{
name|ChangeCache
name|cache
init|=
name|caches
operator|.
name|get
argument_list|(
name|chg
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
name|cache
operator|=
operator|new
name|ChangeCache
argument_list|(
name|chg
argument_list|)
expr_stmt|;
name|caches
operator|.
name|put
argument_list|(
name|chg
argument_list|,
name|cache
argument_list|)
expr_stmt|;
block|}
return|return
name|cache
return|;
block|}
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|detail
specifier|private
name|ChangeDetailCache
name|detail
decl_stmt|;
DECL|method|ChangeCache (Change.Id chg)
specifier|protected
name|ChangeCache
parameter_list|(
name|Change
operator|.
name|Id
name|chg
parameter_list|)
block|{
name|changeId
operator|=
name|chg
expr_stmt|;
block|}
DECL|method|getChangeId ()
specifier|public
name|Change
operator|.
name|Id
name|getChangeId
parameter_list|()
block|{
return|return
name|changeId
return|;
block|}
DECL|method|getChangeDetailCache ()
specifier|public
name|ChangeDetailCache
name|getChangeDetailCache
parameter_list|()
block|{
if|if
condition|(
name|detail
operator|==
literal|null
condition|)
block|{
name|detail
operator|=
operator|new
name|ChangeDetailCache
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
block|}
return|return
name|detail
return|;
block|}
block|}
end_class

end_unit

