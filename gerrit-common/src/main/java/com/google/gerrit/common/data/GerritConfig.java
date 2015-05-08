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

begin_class
DECL|class|GerritConfig
specifier|public
class|class
name|GerritConfig
implements|implements
name|Cloneable
block|{
DECL|field|gitDaemonUrl
specifier|protected
name|String
name|gitDaemonUrl
decl_stmt|;
DECL|field|sshdAddress
specifier|protected
name|String
name|sshdAddress
decl_stmt|;
DECL|field|documentationAvailable
specifier|protected
name|boolean
name|documentationAvailable
decl_stmt|;
DECL|method|getGitDaemonUrl ()
specifier|public
name|String
name|getGitDaemonUrl
parameter_list|()
block|{
return|return
name|gitDaemonUrl
return|;
block|}
DECL|method|setGitDaemonUrl (String url)
specifier|public
name|void
name|setGitDaemonUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
operator|!
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
expr_stmt|;
block|}
name|gitDaemonUrl
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getSshdAddress ()
specifier|public
name|String
name|getSshdAddress
parameter_list|()
block|{
return|return
name|sshdAddress
return|;
block|}
DECL|method|setSshdAddress (final String addr)
specifier|public
name|void
name|setSshdAddress
parameter_list|(
specifier|final
name|String
name|addr
parameter_list|)
block|{
name|sshdAddress
operator|=
name|addr
expr_stmt|;
block|}
DECL|method|isDocumentationAvailable ()
specifier|public
name|boolean
name|isDocumentationAvailable
parameter_list|()
block|{
return|return
name|documentationAvailable
return|;
block|}
DECL|method|setDocumentationAvailable (final boolean available)
specifier|public
name|void
name|setDocumentationAvailable
parameter_list|(
specifier|final
name|boolean
name|available
parameter_list|)
block|{
name|documentationAvailable
operator|=
name|available
expr_stmt|;
block|}
block|}
end_class

end_unit

