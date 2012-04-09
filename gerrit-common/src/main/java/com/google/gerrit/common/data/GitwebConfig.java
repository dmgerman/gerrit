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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_comment
comment|/** Link to an external gitweb server. */
end_comment

begin_class
DECL|class|GitwebConfig
specifier|public
class|class
name|GitwebConfig
block|{
DECL|field|baseUrl
specifier|public
name|String
name|baseUrl
decl_stmt|;
DECL|field|type
specifier|public
name|GitWebType
name|type
decl_stmt|;
DECL|method|GitwebConfig ()
specifier|protected
name|GitwebConfig
parameter_list|()
block|{   }
DECL|method|GitwebConfig (final String base, final GitWebType gitWebType)
specifier|public
name|GitwebConfig
parameter_list|(
specifier|final
name|String
name|base
parameter_list|,
specifier|final
name|GitWebType
name|gitWebType
parameter_list|)
block|{
name|baseUrl
operator|=
name|base
expr_stmt|;
name|type
operator|=
name|gitWebType
expr_stmt|;
block|}
block|}
end_class

end_unit

