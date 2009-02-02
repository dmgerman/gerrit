begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|patches
operator|.
name|PatchScreen
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
name|client
operator|.
name|reviewdb
operator|.
name|Patch
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
comment|/** Detail necessary to display {@link PatchScreen}. */
end_comment

begin_class
DECL|class|BasePatchDetail
specifier|public
specifier|abstract
class|class
name|BasePatchDetail
block|{
DECL|field|accounts
specifier|protected
name|AccountInfoCache
name|accounts
decl_stmt|;
DECL|field|patch
specifier|protected
name|Patch
name|patch
decl_stmt|;
DECL|field|history
specifier|protected
name|List
argument_list|<
name|Patch
argument_list|>
name|history
decl_stmt|;
DECL|method|BasePatchDetail ()
specifier|protected
name|BasePatchDetail
parameter_list|()
block|{   }
DECL|method|BasePatchDetail (final Patch p, final AccountInfoCache aic)
specifier|public
name|BasePatchDetail
parameter_list|(
specifier|final
name|Patch
name|p
parameter_list|,
specifier|final
name|AccountInfoCache
name|aic
parameter_list|)
block|{
name|patch
operator|=
name|p
expr_stmt|;
name|accounts
operator|=
name|aic
expr_stmt|;
block|}
DECL|method|getAccounts ()
specifier|public
name|AccountInfoCache
name|getAccounts
parameter_list|()
block|{
return|return
name|accounts
return|;
block|}
DECL|method|getPatch ()
specifier|public
name|Patch
name|getPatch
parameter_list|()
block|{
return|return
name|patch
return|;
block|}
DECL|method|getHistory ()
specifier|public
name|List
argument_list|<
name|Patch
argument_list|>
name|getHistory
parameter_list|()
block|{
return|return
name|history
return|;
block|}
DECL|method|setHistory (final List<Patch> ids)
specifier|public
name|void
name|setHistory
parameter_list|(
specifier|final
name|List
argument_list|<
name|Patch
argument_list|>
name|ids
parameter_list|)
block|{
name|history
operator|=
name|ids
expr_stmt|;
block|}
block|}
end_class

end_unit

