begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|gerrit
operator|.
name|client
operator|.
name|data
operator|.
name|GerritConfig
import|;
end_import

begin_class
DECL|class|Common
specifier|public
class|class
name|Common
block|{
DECL|field|config
specifier|private
specifier|static
name|GerritConfig
name|config
decl_stmt|;
comment|/** Get the public configuration data used by this Gerrit instance. */
DECL|method|getGerritConfig ()
specifier|public
specifier|static
name|GerritConfig
name|getGerritConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setGerritConfig (final GerritConfig imp)
specifier|public
specifier|static
name|void
name|setGerritConfig
parameter_list|(
specifier|final
name|GerritConfig
name|imp
parameter_list|)
block|{
name|config
operator|=
name|imp
expr_stmt|;
block|}
block|}
end_class

end_unit

