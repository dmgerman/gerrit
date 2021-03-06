begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.documentation
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|documentation
package|;
end_package

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|ast
operator|.
name|Heading
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|ast
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|ext
operator|.
name|anchorlink
operator|.
name|AnchorLink
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|ext
operator|.
name|anchorlink
operator|.
name|internal
operator|.
name|AnchorLinkNodeRenderer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|html
operator|.
name|HtmlRenderer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|html
operator|.
name|HtmlRenderer
operator|.
name|HtmlRendererExtension
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|html
operator|.
name|HtmlWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|html
operator|.
name|renderer
operator|.
name|DelegatingNodeRendererFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|html
operator|.
name|renderer
operator|.
name|NodeRenderer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|html
operator|.
name|renderer
operator|.
name|NodeRendererContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|html
operator|.
name|renderer
operator|.
name|NodeRendererFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|html
operator|.
name|renderer
operator|.
name|NodeRenderingHandler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|profiles
operator|.
name|pegdown
operator|.
name|Extensions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|profiles
operator|.
name|pegdown
operator|.
name|PegdownOptionsAdapter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|util
operator|.
name|options
operator|.
name|DataHolder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|vladsch
operator|.
name|flexmark
operator|.
name|util
operator|.
name|options
operator|.
name|MutableDataHolder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
DECL|class|MarkdownFormatterHeader
specifier|public
class|class
name|MarkdownFormatterHeader
block|{
DECL|class|HeadingExtension
specifier|static
class|class
name|HeadingExtension
implements|implements
name|HtmlRendererExtension
block|{
annotation|@
name|Override
DECL|method|rendererOptions (final MutableDataHolder options)
specifier|public
name|void
name|rendererOptions
parameter_list|(
specifier|final
name|MutableDataHolder
name|options
parameter_list|)
block|{
comment|// add any configuration settings to options you want to apply to everything, here
block|}
annotation|@
name|Override
DECL|method|extend (final HtmlRenderer.Builder rendererBuilder, final String rendererType)
specifier|public
name|void
name|extend
parameter_list|(
specifier|final
name|HtmlRenderer
operator|.
name|Builder
name|rendererBuilder
parameter_list|,
specifier|final
name|String
name|rendererType
parameter_list|)
block|{
name|rendererBuilder
operator|.
name|nodeRendererFactory
argument_list|(
operator|new
name|HeadingNodeRenderer
operator|.
name|Factory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|create ()
specifier|static
name|HeadingExtension
name|create
parameter_list|()
block|{
return|return
operator|new
name|HeadingExtension
argument_list|()
return|;
block|}
block|}
DECL|class|HeadingNodeRenderer
specifier|static
class|class
name|HeadingNodeRenderer
implements|implements
name|NodeRenderer
block|{
DECL|method|HeadingNodeRenderer ()
specifier|public
name|HeadingNodeRenderer
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|getNodeRenderingHandlers ()
specifier|public
name|Set
argument_list|<
name|NodeRenderingHandler
argument_list|<
name|?
argument_list|>
argument_list|>
name|getNodeRenderingHandlers
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|NodeRenderingHandler
argument_list|<>
argument_list|(
name|AnchorLink
operator|.
name|class
argument_list|,
parameter_list|(
name|node
parameter_list|,
name|context
parameter_list|,
name|html
parameter_list|)
lambda|->
name|HeadingNodeRenderer
operator|.
name|this
operator|.
name|render
argument_list|(
name|node
argument_list|,
name|context
argument_list|)
argument_list|)
argument_list|,
operator|new
name|NodeRenderingHandler
argument_list|<>
argument_list|(
name|Heading
operator|.
name|class
argument_list|,
name|HeadingNodeRenderer
operator|.
name|this
operator|::
name|render
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|render (final AnchorLink node, final NodeRendererContext context)
name|void
name|render
parameter_list|(
specifier|final
name|AnchorLink
name|node
parameter_list|,
specifier|final
name|NodeRendererContext
name|context
parameter_list|)
block|{
name|Node
name|parent
init|=
name|node
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|instanceof
name|Heading
operator|&&
operator|(
operator|(
name|Heading
operator|)
name|parent
operator|)
operator|.
name|getLevel
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// render without anchor link
name|context
operator|.
name|renderChildren
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|delegateRender
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|haveExtension (int extensions, int flags)
specifier|static
name|boolean
name|haveExtension
parameter_list|(
name|int
name|extensions
parameter_list|,
name|int
name|flags
parameter_list|)
block|{
return|return
operator|(
name|extensions
operator|&
name|flags
operator|)
operator|!=
literal|0
return|;
block|}
DECL|method|haveAllExtensions (int extensions, int flags)
specifier|static
name|boolean
name|haveAllExtensions
parameter_list|(
name|int
name|extensions
parameter_list|,
name|int
name|flags
parameter_list|)
block|{
return|return
operator|(
name|extensions
operator|&
name|flags
operator|)
operator|==
name|flags
return|;
block|}
DECL|method|render (final Heading node, final NodeRendererContext context, final HtmlWriter html)
name|void
name|render
parameter_list|(
specifier|final
name|Heading
name|node
parameter_list|,
specifier|final
name|NodeRendererContext
name|context
parameter_list|,
specifier|final
name|HtmlWriter
name|html
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getLevel
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// render without anchor link
specifier|final
name|int
name|extensions
init|=
name|context
operator|.
name|getOptions
argument_list|()
operator|.
name|get
argument_list|(
name|PegdownOptionsAdapter
operator|.
name|PEGDOWN_EXTENSIONS
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|getHtmlOptions
argument_list|()
operator|.
name|renderHeaderId
operator|||
name|haveExtension
argument_list|(
name|extensions
argument_list|,
name|Extensions
operator|.
name|ANCHORLINKS
argument_list|)
operator|||
name|haveAllExtensions
argument_list|(
name|extensions
argument_list|,
name|Extensions
operator|.
name|EXTANCHORLINKS
operator||
name|Extensions
operator|.
name|EXTANCHORLINKS_WRAP
argument_list|)
condition|)
block|{
name|String
name|id
init|=
name|context
operator|.
name|getNodeId
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|html
operator|.
name|attr
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|context
operator|.
name|getHtmlOptions
argument_list|()
operator|.
name|sourcePositionParagraphLines
condition|)
block|{
name|html
operator|.
name|srcPos
argument_list|(
name|node
operator|.
name|getChars
argument_list|()
argument_list|)
operator|.
name|withAttr
argument_list|()
operator|.
name|tagLine
argument_list|(
literal|"h"
operator|+
name|node
operator|.
name|getLevel
argument_list|()
argument_list|,
parameter_list|()
lambda|->
block|{
name|html
operator|.
name|srcPos
argument_list|(
name|node
operator|.
name|getText
argument_list|()
argument_list|)
operator|.
name|withAttr
argument_list|()
operator|.
name|tag
argument_list|(
literal|"span"
argument_list|)
expr_stmt|;
name|context
operator|.
name|renderChildren
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|html
operator|.
name|tag
argument_list|(
literal|"/span"
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|html
operator|.
name|srcPos
argument_list|(
name|node
operator|.
name|getText
argument_list|()
argument_list|)
operator|.
name|withAttr
argument_list|()
operator|.
name|tagLine
argument_list|(
literal|"h"
operator|+
name|node
operator|.
name|getLevel
argument_list|()
argument_list|,
parameter_list|()
lambda|->
name|context
operator|.
name|renderChildren
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|context
operator|.
name|delegateRender
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|Factory
specifier|public
specifier|static
class|class
name|Factory
implements|implements
name|DelegatingNodeRendererFactory
block|{
annotation|@
name|Override
DECL|method|create (final DataHolder options)
specifier|public
name|NodeRenderer
name|create
parameter_list|(
specifier|final
name|DataHolder
name|options
parameter_list|)
block|{
return|return
operator|new
name|HeadingNodeRenderer
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getDelegates ()
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|NodeRendererFactory
argument_list|>
argument_list|>
name|getDelegates
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|NodeRendererFactory
argument_list|>
argument_list|>
name|delegates
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|delegates
operator|.
name|add
argument_list|(
name|AnchorLinkNodeRenderer
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|delegates
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

