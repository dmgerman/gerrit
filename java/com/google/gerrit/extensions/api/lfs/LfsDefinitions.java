begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.lfs
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|lfs
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
name|Joiner
import|;
end_import

begin_class
DECL|class|LfsDefinitions
specifier|public
specifier|final
class|class
name|LfsDefinitions
block|{
DECL|field|CONTENTTYPE_VND_GIT_LFS_JSON
specifier|public
specifier|static
specifier|final
name|String
name|CONTENTTYPE_VND_GIT_LFS_JSON
init|=
literal|"application/vnd.git-lfs+json; charset=utf-8"
decl_stmt|;
DECL|field|LFS_OBJECTS_PATH
specifier|public
specifier|static
specifier|final
name|String
name|LFS_OBJECTS_PATH
init|=
literal|"objects/batch"
decl_stmt|;
DECL|field|LFS_LOCKS_PATH_REGEX
specifier|public
specifier|static
specifier|final
name|String
name|LFS_LOCKS_PATH_REGEX
init|=
literal|"locks(?:/(.*)(?:/unlock))?"
decl_stmt|;
DECL|field|LFS_VERIFICATION_PATH
specifier|public
specifier|static
specifier|final
name|String
name|LFS_VERIFICATION_PATH
init|=
literal|"locks/verify"
decl_stmt|;
DECL|field|LFS_UNIFIED_PATHS_REGEX
specifier|public
specifier|static
specifier|final
name|String
name|LFS_UNIFIED_PATHS_REGEX
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|'|'
argument_list|)
operator|.
name|join
argument_list|(
name|LFS_OBJECTS_PATH
argument_list|,
name|LFS_LOCKS_PATH_REGEX
argument_list|,
name|LFS_VERIFICATION_PATH
argument_list|)
decl_stmt|;
DECL|field|LFS_URL_WO_AUTH_REGEX_TEAMPLATE
specifier|public
specifier|static
specifier|final
name|String
name|LFS_URL_WO_AUTH_REGEX_TEAMPLATE
init|=
literal|"(?:/p/|/)(.+)(?:/info/lfs/)(?:%s)$"
decl_stmt|;
DECL|field|LFS_URL_WO_AUTH_REGEX
specifier|public
specifier|static
specifier|final
name|String
name|LFS_URL_WO_AUTH_REGEX
init|=
name|String
operator|.
name|format
argument_list|(
name|LFS_URL_WO_AUTH_REGEX_TEAMPLATE
argument_list|,
name|LFS_UNIFIED_PATHS_REGEX
argument_list|)
decl_stmt|;
DECL|field|LFS_URL_REGEX_TEMPLATE
specifier|public
specifier|static
specifier|final
name|String
name|LFS_URL_REGEX_TEMPLATE
init|=
literal|"^(?:/a)?"
operator|+
name|LFS_URL_WO_AUTH_REGEX_TEAMPLATE
decl_stmt|;
DECL|field|LFS_URL_REGEX
specifier|public
specifier|static
specifier|final
name|String
name|LFS_URL_REGEX
init|=
name|String
operator|.
name|format
argument_list|(
name|LFS_URL_REGEX_TEMPLATE
argument_list|,
name|LFS_UNIFIED_PATHS_REGEX
argument_list|)
decl_stmt|;
DECL|method|LfsDefinitions ()
specifier|private
name|LfsDefinitions
parameter_list|()
block|{}
block|}
end_class

end_unit

