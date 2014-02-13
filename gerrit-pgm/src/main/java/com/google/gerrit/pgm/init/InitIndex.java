begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm.init
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
name|LuceneChangeIndex
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
name|util
operator|.
name|ConsoleUI
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
name|SitePaths
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
name|ChangeSchemas
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
name|IndexModule
operator|.
name|IndexType
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
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/** Initialize the {@code index} configuration section. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|InitIndex
class|class
name|InitIndex
implements|implements
name|InitStep
block|{
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|Section
name|index
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
DECL|field|initFlags
specifier|private
specifier|final
name|InitFlags
name|initFlags
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitIndex (ConsoleUI ui, Section.Factory sections, SitePaths site, InitFlags initFlags)
name|InitIndex
parameter_list|(
name|ConsoleUI
name|ui
parameter_list|,
name|Section
operator|.
name|Factory
name|sections
parameter_list|,
name|SitePaths
name|site
parameter_list|,
name|InitFlags
name|initFlags
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|sections
operator|.
name|get
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
name|this
operator|.
name|initFlags
operator|=
name|initFlags
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|IOException
block|{
name|ui
operator|.
name|header
argument_list|(
literal|"Index"
argument_list|)
expr_stmt|;
name|IndexType
name|type
init|=
name|index
operator|.
name|select
argument_list|(
literal|"Type"
argument_list|,
literal|"type"
argument_list|,
name|IndexType
operator|.
name|LUCENE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|IndexType
operator|.
name|SOLR
condition|)
block|{
name|index
operator|.
name|string
argument_list|(
literal|"Solr Index URL"
argument_list|,
literal|"url"
argument_list|,
literal|"localhost:9983"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|site
operator|.
name|isNew
operator|&&
name|type
operator|==
name|IndexType
operator|.
name|LUCENE
condition|)
block|{
name|LuceneChangeIndex
operator|.
name|setReady
argument_list|(
name|site
argument_list|,
name|ChangeSchemas
operator|.
name|getLatest
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|String
name|message
init|=
name|String
operator|.
name|format
argument_list|(
literal|"\nThe index must be %sbuilt before starting Gerrit:\n"
operator|+
literal|"  java -jar gerrit.war reindex -d site_path\n"
argument_list|,
name|site
operator|.
name|isNew
condition|?
literal|""
else|:
literal|"re"
argument_list|)
decl_stmt|;
name|ui
operator|.
name|message
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|initFlags
operator|.
name|autoStart
operator|=
literal|false
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|postRun ()
specifier|public
name|void
name|postRun
parameter_list|()
throws|throws
name|Exception
block|{   }
block|}
end_class

end_unit

