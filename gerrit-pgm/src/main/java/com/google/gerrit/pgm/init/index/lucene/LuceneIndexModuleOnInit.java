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
DECL|package|com.google.gerrit.pgm.init.index.lucene
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
operator|.
name|index
operator|.
name|lucene
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
name|lucene
operator|.
name|LuceneAccountIndex
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
name|lucene
operator|.
name|LuceneGroupIndex
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
name|pgm
operator|.
name|init
operator|.
name|index
operator|.
name|IndexModuleOnInit
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
name|account
operator|.
name|AccountIndex
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
name|group
operator|.
name|GroupIndex
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
name|AbstractModule
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
name|assistedinject
operator|.
name|FactoryModuleBuilder
import|;
end_import

begin_class
DECL|class|LuceneIndexModuleOnInit
specifier|public
class|class
name|LuceneIndexModuleOnInit
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|install
argument_list|(
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|implement
argument_list|(
name|AccountIndex
operator|.
name|class
argument_list|,
name|LuceneAccountIndex
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|(
name|AccountIndex
operator|.
name|Factory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|implement
argument_list|(
name|GroupIndex
operator|.
name|class
argument_list|,
name|LuceneGroupIndex
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|(
name|GroupIndex
operator|.
name|Factory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|IndexModuleOnInit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

