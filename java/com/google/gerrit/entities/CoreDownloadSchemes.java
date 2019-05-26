begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.entities
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
package|;
end_package

begin_comment
comment|/** Download scheme string constants supported by the download-commands core plugin. */
end_comment

begin_class
DECL|class|CoreDownloadSchemes
specifier|public
class|class
name|CoreDownloadSchemes
block|{
DECL|field|ANON_GIT
specifier|public
specifier|static
specifier|final
name|String
name|ANON_GIT
init|=
literal|"git"
decl_stmt|;
DECL|field|ANON_HTTP
specifier|public
specifier|static
specifier|final
name|String
name|ANON_HTTP
init|=
literal|"anonymous http"
decl_stmt|;
DECL|field|HTTP
specifier|public
specifier|static
specifier|final
name|String
name|HTTP
init|=
literal|"http"
decl_stmt|;
DECL|field|SSH
specifier|public
specifier|static
specifier|final
name|String
name|SSH
init|=
literal|"ssh"
decl_stmt|;
DECL|field|REPO_DOWNLOAD
specifier|public
specifier|static
specifier|final
name|String
name|REPO_DOWNLOAD
init|=
literal|"repo"
decl_stmt|;
DECL|method|CoreDownloadSchemes ()
specifier|private
name|CoreDownloadSchemes
parameter_list|()
block|{}
block|}
end_class

end_unit

