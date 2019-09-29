begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
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
name|Iterables
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
name|index
operator|.
name|change
operator|.
name|ChangeSchemaDefinitions
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
name|testing
operator|.
name|ConfigSuite
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
name|testing
operator|.
name|IndexConfig
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
name|testing
operator|.
name|IndexVersions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_class
DECL|class|LuceneQueryChangesPreviousIndexVersionTest
specifier|public
class|class
name|LuceneQueryChangesPreviousIndexVersionTest
extends|extends
name|LuceneQueryChangesTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Default
DECL|method|againstPreviousIndexVersion ()
specifier|public
specifier|static
name|Config
name|againstPreviousIndexVersion
parameter_list|()
block|{
comment|// the current schema version is already tested by the inherited default config suite
return|return
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|IndexVersions
operator|.
name|asConfigMap
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|INSTANCE
argument_list|,
name|IndexVersions
operator|.
name|getWithoutLatest
argument_list|(
name|ChangeSchemaDefinitions
operator|.
name|INSTANCE
argument_list|)
argument_list|,
literal|"againstIndexVersion"
argument_list|,
name|IndexConfig
operator|.
name|createForLucene
argument_list|()
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

