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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|ImmutableMap
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
name|ImmutableSet
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
name|Iterables
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
name|Sets
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
name|server
operator|.
name|config
operator|.
name|DownloadConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|AllowedFormats
specifier|public
class|class
name|AllowedFormats
block|{
DECL|field|extensions
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|ArchiveFormat
argument_list|>
name|extensions
decl_stmt|;
DECL|field|allowed
specifier|final
name|ImmutableSet
argument_list|<
name|ArchiveFormat
argument_list|>
name|allowed
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllowedFormats (DownloadConfig cfg)
name|AllowedFormats
parameter_list|(
name|DownloadConfig
name|cfg
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ArchiveFormat
argument_list|>
name|exts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ArchiveFormat
name|format
range|:
name|cfg
operator|.
name|getArchiveFormats
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|ext
range|:
name|format
operator|.
name|getSuffixes
argument_list|()
control|)
block|{
name|exts
operator|.
name|put
argument_list|(
name|ext
argument_list|,
name|format
argument_list|)
expr_stmt|;
block|}
name|exts
operator|.
name|put
argument_list|(
name|format
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|format
argument_list|)
expr_stmt|;
block|}
name|extensions
operator|=
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|exts
argument_list|)
expr_stmt|;
comment|// Zip is not supported because it may be interpreted by a Java plugin as a
comment|// valid JAR file, whose code would have access to cookies on the domain.
name|allowed
operator|=
name|Sets
operator|.
name|immutableEnumSet
argument_list|(
name|Iterables
operator|.
name|filter
argument_list|(
name|cfg
operator|.
name|getArchiveFormats
argument_list|()
argument_list|,
name|f
lambda|->
name|f
operator|!=
name|ArchiveFormat
operator|.
name|ZIP
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getAllowed ()
specifier|public
name|Set
argument_list|<
name|ArchiveFormat
argument_list|>
name|getAllowed
parameter_list|()
block|{
return|return
name|allowed
return|;
block|}
DECL|method|getExtensions ()
specifier|public
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|ArchiveFormat
argument_list|>
name|getExtensions
parameter_list|()
block|{
return|return
name|extensions
return|;
block|}
block|}
end_class

end_unit

